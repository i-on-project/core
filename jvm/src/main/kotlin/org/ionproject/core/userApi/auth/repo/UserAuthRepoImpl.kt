package org.ionproject.core.userApi.auth.repo

import io.jsonwebtoken.Jwts
import kotlinx.coroutines.runBlocking
import org.ionproject.core.common.Uri
import org.ionproject.core.common.jwt.JwtSecretKeyProvider
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.toNullable
import org.ionproject.core.userApi.auth.AuthRequestAlreadyExistsException
import org.ionproject.core.userApi.auth.AuthRequestAlreadyVerifiedException
import org.ionproject.core.userApi.auth.AuthRequestExpiredException
import org.ionproject.core.userApi.auth.AuthRequestInvalidClientIdException
import org.ionproject.core.userApi.auth.AuthRequestInvalidScopesException
import org.ionproject.core.userApi.auth.AuthRequestInvalidSecretException
import org.ionproject.core.userApi.auth.AuthRequestNotFoundException
import org.ionproject.core.userApi.auth.AuthRequestPendingException
import org.ionproject.core.userApi.auth.AuthRequestUserCreationException
import org.ionproject.core.userApi.auth.InvalidRefreshToken
import org.ionproject.core.userApi.auth.RefreshTokenRateLimitException
import org.ionproject.core.userApi.auth.UserTokenNotFoundException
import org.ionproject.core.userApi.auth.model.AuthClient
import org.ionproject.core.userApi.auth.model.AuthMethodInput
import org.ionproject.core.userApi.auth.model.AuthRequest
import org.ionproject.core.userApi.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.userApi.auth.model.AuthRequestHelper
import org.ionproject.core.userApi.auth.model.AuthRequestOutput
import org.ionproject.core.userApi.auth.model.AuthRequestScope
import org.ionproject.core.userApi.auth.model.AuthScope
import org.ionproject.core.userApi.auth.model.AuthSuccessfulResponse
import org.ionproject.core.userApi.auth.registry.AuthMethodRegistry
import org.ionproject.core.userApi.auth.registry.AuthNotificationRegistry
import org.ionproject.core.userApi.auth.sql.AuthData
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserToken
import org.ionproject.core.userApi.user.model.UserTokenInfo
import org.ionproject.core.userApi.user.model.UserTokenScope
import org.ionproject.core.userApi.user.sql.UserData
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.stereotype.Repository
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Repository
class UserAuthRepoImpl(
    val tm: TransactionManager,
    val methodRegistry: AuthMethodRegistry,
    val notificationRegistry: AuthNotificationRegistry,
    jwtSecretKeyProvider: JwtSecretKeyProvider
) : UserAuthRepo {

    companion object {
        private const val TOKEN_REFRESH_RATE = 9

        private val base64Encoder = Base64.getUrlEncoder()
            .withoutPadding()

        private val accessTokenDuration = Duration.ofHours(3)
        private val idTokenDuration = Duration.ofHours(1)
        private val tokenRefreshRateDuration = Duration.ofMinutes(
            accessTokenDuration.toMinutes() / TOKEN_REFRESH_RATE
        )
    }

    private val secretKey: SecretKey by jwtSecretKeyProvider

    override fun getAuthMethods() = methodRegistry.authMethods

    override fun addAuthRequest(userAgent: String, input: AuthMethodInput) =
        tm.run {
            // get & check if the client is valid
            val client = getClientById(input.clientId, it)

            // check if the notification method is valid
            notificationRegistry.findOrThrow(input.notificationMethod)

            // get & check if the scopes are available
            val scopes = checkRequestedScopes(input.scope, it)

            val req = findAuthRequestByClientAndEmail(input.clientId, input.email, it)
            if (req != null) {
                if (req.hasExpired())
                    removeAuthRequest(req.authRequestId, it)
                else
                    throw AuthRequestAlreadyExistsException(input.email)
            }

            val authRequestId = generateUUID()
            val helper = AuthRequestHelper(
                authRequestId,
                generateSecret(),
                input.clientId,
                client.clientName,
                userAgent,
                input.notificationMethod,
                input.email
            )

            createAuthRequest(helper, scopes, it)

            runBlocking {
                val methodSolver = methodRegistry[input.type]
                if (!methodSolver.create) {
                    it.createQuery(UserData.GET_USER_BY_EMAIL)
                        .bind(UserData.EMAIL, input.email)
                        .mapTo<User>()
                        .findOne()
                        .toNullable() ?: throw AuthRequestUserCreationException()
                }

                methodSolver.solve(helper)
            }

            AuthRequestAcknowledgement(
                authRequestId,
                helper.expiresIn
            )
        }

    override fun verifyAuthRequest(authRequestId: String, secretId: String) =
        tm.run {
            val authReq = getAuthRequestAndVerifySecret(authRequestId, secretId, it)
            if (authReq.verified)
                throw AuthRequestAlreadyVerifiedException()

            if (authReq.hasExpired()) {
                removeAuthRequest(authRequestId, it)
                throw AuthRequestExpiredException()
            }

            it.createUpdate(AuthData.VERIFY_AUTH_REQUEST)
                .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
                .execute()

            Unit
        }

    override fun checkAuthRequest(authRequestId: String) =
        tm.run {
            val authReq = getAuthRequest(authRequestId, it)
            if (authReq.verified) {
                val user = getOrCreateUser(authReq.email, it)
                val userToken = createUserToken(user, authReq, it)
                val idToken = generateIdToken(user, authReq.clientId)

                removeAuthRequest(authRequestId, it)
                AuthSuccessfulResponse.from(userToken, idToken)
            } else {
                if (authReq.hasExpired()) {
                    // the request has expired
                    removeAuthRequest(authRequestId, it)
                    throw AuthRequestExpiredException()
                }

                throw AuthRequestPendingException()
            }
        }

    override fun getAuthRequest(authRequestId: String) =
        tm.run {
            val authRequest = getAuthRequest(authRequestId, it)
            val client = getClientById(authRequest.clientId, it)
            val scopes = getRequestScopes(authRequestId, it)

            AuthRequestOutput(
                authRequestId,
                authRequest.expiresOn,
                client,
                scopes,
                Uri.authVerifyUri
            )
        }

    override fun getTokenInfo(accessToken: String) =
        tm.run {
            // checks if the specified token exists
            val token = getUserToken(accessToken, it)
            val scopes = it.createQuery(UserData.GET_USER_TOKEN_SCOPES)
                .bind(UserData.ACCESS_TOKEN, accessToken)
                .mapTo<UserTokenScope>()
                .toSet()

            UserTokenInfo(
                token,
                scopes
            )
        }

    override fun refreshAccessToken(accessToken: String, refreshToken: String) =
        tm.run {
            val userToken = getUserToken(accessToken, it)
            if (userToken.refreshToken != refreshToken)
                throw InvalidRefreshToken()

            val minutesSinceCreation = Duration.between(userToken.updatedAt, Instant.now())
            if (minutesSinceCreation < tokenRefreshRateDuration)
                throw RefreshTokenRateLimitException(tokenRefreshRateDuration.toMinutes())

            val newToken = generateUserToken(
                userToken.userId,
                userToken.clientId
            )

            it.createUpdate(UserData.REFRESH_USER_TOKEN)
                .bindKotlin(newToken)
                .execute()

            val user = it.createQuery(UserData.GET_USER_BY_ID)
                .bind(UserData.USER_ID, newToken.userId)
                .mapTo<User>()
                .findOne()
                .get()

            val idToken = generateIdToken(user, newToken.clientId)
            AuthSuccessfulResponse.from(newToken, idToken)
        }

    override fun revokeAccessToken(accessToken: String, refreshToken: String) =
        tm.run {
            val userToken = getUserToken(accessToken, it)
            revokeUserToken(userToken, it)
        }

    private fun checkRequestedScopes(scopes: String, handle: Handle): List<String> {
        val scopeList = scopes.split(" ")
        val availableScopes = handle.createQuery(AuthData.GET_AVAILABLE_SCOPES)
            .mapTo<AuthScope>()
            .map { it.scope }
            .toSet()

        val invalidScopes = scopeList.filter {
            !availableScopes.contains(it)
        }

        if (invalidScopes.isNotEmpty())
            throw AuthRequestInvalidScopesException(invalidScopes)

        return scopeList
    }

    private fun getClientById(clientId: String, handle: Handle): AuthClient {
        return handle.createQuery(AuthData.GET_CLIENT_BY_ID)
            .bind(AuthData.CLIENT_ID, clientId)
            .mapTo<AuthClient>()
            .findOne()
            .toNullable() ?: throw AuthRequestInvalidClientIdException(clientId)
    }

    private fun createAuthRequest(helper: AuthRequestHelper, scopes: List<String>, handle: Handle) {
        handle.createUpdate(AuthData.INSERT_AUTH_REQUEST)
            .bindKotlin(helper)
            .execute()

        val batch = handle.prepareBatch(AuthData.INSERT_REQUEST_SCOPES)
        scopes.map { s -> AuthRequestScope(helper.authRequestId, s) }
            .forEach { ars ->
                batch.bindKotlin(ars)
                    .add()
            }

        batch.execute()
    }

    private fun getAuthRequest(authRequestId: String, handle: Handle): AuthRequest {
        return handle.createQuery(AuthData.GET_AUTH_REQUEST)
            .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
            .mapTo<AuthRequest>()
            .findOne()
            .toNullable() ?: throw AuthRequestNotFoundException()
    }

    private fun getAuthRequestAndVerifySecret(authRequestId: String, secretId: String, handle: Handle): AuthRequest {
        val authReq = getAuthRequest(authRequestId, handle)
        if (authReq.secretId != secretId) {
            // if the secret is wrong a malicious user is probably trying to compromise
            // this user account, so we remove the auth request and report it
            removeAuthRequest(authRequestId, handle)
            throw AuthRequestInvalidSecretException()
        }

        return authReq
    }

    private fun removeAuthRequest(authRequestId: String, handle: Handle) {
        handle.createUpdate(AuthData.REMOVE_AUTH_REQUEST)
            .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
            .execute()
    }

    private fun getRequestScopes(authRequestId: String, handle: Handle): List<AuthScope> {
        return handle.createQuery(AuthData.GET_REQUEST_SCOPES)
            .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
            .mapTo<AuthScope>()
            .list()
    }

    private fun getOrCreateUser(email: String, handle: Handle): User {
        var user = handle.createQuery(UserData.GET_USER_BY_EMAIL)
            .bind(UserData.EMAIL, email)
            .mapTo<User>()
            .findOne()
            .toNullable()

        if (user == null) {
            user = User(generateUUID(), email)
            handle.createUpdate(UserData.INSERT_USER)
                .bindKotlin(user)
                .execute()
        }

        return user
    }

    private fun findAuthRequestByClientAndEmail(clientId: String, email: String, handle: Handle): AuthRequest? {
        return handle.createQuery(AuthData.GET_AUTH_REQUEST_BY_CLIENT_AND_EMAIL)
            .bind(AuthData.CLIENT_ID, clientId)
            .bind(AuthData.EMAIL, email)
            .mapTo<AuthRequest>()
            .findOne()
            .toNullable()
    }

    private fun getUserToken(accessToken: String, handle: Handle): UserToken {
        return handle.createQuery(UserData.GET_USER_TOKEN)
            .bind(UserData.ACCESS_TOKEN, accessToken)
            .mapTo<UserToken>()
            .findOne()
            .toNullable() ?: throw UserTokenNotFoundException()
    }

    private fun findUserTokenByClient(user: User, clientId: String, handle: Handle): UserToken? {
        return handle.createQuery(UserData.GET_USER_TOKEN_BY_CLIENT)
            .bind(UserData.CLIENT_ID, clientId)
            .bind(UserData.USER_ID, user.userId)
            .mapTo<UserToken>()
            .findOne()
            .toNullable()
    }

    private fun revokeUserToken(userToken: UserToken, handle: Handle) {
        handle.createUpdate(UserData.REVOKE_USER_TOKEN)
            .bind(UserData.ACCESS_TOKEN, userToken.accessToken)
            .execute()
    }

    private fun createUserToken(user: User, authReq: AuthRequest, handle: Handle): UserToken {
        val existingToken = findUserTokenByClient(user, authReq.clientId, handle)
        if (existingToken != null)
            revokeUserToken(existingToken, handle)

        val userToken = generateUserToken(user.userId, authReq.clientId)
        handle.createUpdate(UserData.INSERT_USER_TOKEN)
            .bindKotlin(userToken)
            .execute()

        val scopes = getRequestScopes(authReq.authRequestId, handle)
        val batch = handle.prepareBatch(UserData.INSERT_USER_TOKEN_SCOPE)

        scopes.forEach {
            batch.bindKotlin(
                UserTokenScope(
                    userToken.accessToken,
                    it.scope
                )
            ).add()
        }

        batch.execute()
        return userToken
    }

    private fun generateUserToken(userId: String, clientId: String): UserToken {
        val accessToken = generateSecret()
        val refreshToken = generateSecret()
        val accessTokenExpiration = Instant.now().plus(accessTokenDuration)

        return UserToken(
            userId,
            clientId,
            accessToken,
            refreshToken,
            accessTokenExpiration
        )
    }

    private fun generateIdToken(user: User, clientId: String): String {
        val issuedAt = Instant.now()
        val expirationDate = Date.from(issuedAt.plus(idTokenDuration))

        return Jwts.builder()
            .setExpiration(expirationDate)
            .setAudience(clientId)
            .setIssuedAt(Date.from(issuedAt))
            .setIssuer("${Uri.baseUrl}${Uri.apiBase}")
            .setSubject(user.userId)
            .claim("email", user.email)
            .claim("name", user.name)
            .signWith(secretKey)
            .compact()
    }

    private fun AuthRequest.hasExpired() = expiresOn.isBefore(Instant.now())

    private fun generateUUID() = UUID.randomUUID().toString()

    private fun generateSecret(): String {
        val bytes = ByteArray(64)

        val random = SecureRandom()
        random.nextBytes(bytes)

        return base64Encoder.encodeToString(bytes)
    }
}
