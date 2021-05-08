package org.ionproject.core.user.auth.repo

import io.jsonwebtoken.Jwts
import kotlinx.coroutines.runBlocking
import org.ionproject.core.common.Uri
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.toNullable
import org.ionproject.core.user.auth.InvalidClientIdException
import org.ionproject.core.user.auth.InvalidScopesException
import org.ionproject.core.user.auth.InvalidUserCreationMethodException
import org.ionproject.core.user.auth.RequestAlreadyValidatedException
import org.ionproject.core.user.auth.RequestTokenExpiredException
import org.ionproject.core.user.auth.RequestTokenInvalidRequestException
import org.ionproject.core.user.auth.RequestTokenPendingException
import org.ionproject.core.user.auth.model.AuthClient
import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthRequest
import org.ionproject.core.user.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.user.auth.model.AuthRequestHelper
import org.ionproject.core.user.auth.model.AuthRequestOutput
import org.ionproject.core.user.auth.model.AuthRequestScope
import org.ionproject.core.user.auth.model.AuthScope
import org.ionproject.core.user.auth.model.AuthSuccessfulResponse
import org.ionproject.core.user.auth.registry.AuthMethodRegistry
import org.ionproject.core.user.auth.registry.AuthNotificationRegistry
import org.ionproject.core.user.auth.sql.AuthData
import org.ionproject.core.user.common.jwt.JwtSecretKeyProvider
import org.ionproject.core.user.common.model.User
import org.ionproject.core.user.common.model.UserToken
import org.ionproject.core.user.common.model.UserTokenScope
import org.ionproject.core.user.common.sql.UserData
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
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

    private val base64Encoder = Base64.getUrlEncoder()
        .withoutPadding()

    private val secretKey: SecretKey by jwtSecretKeyProvider

    override fun getAuthMethods() = methodRegistry.authMethods

    override fun addAuthRequest(userAgent: String, input: AuthMethodInput) =
        tm.run(TransactionIsolationLevel.SERIALIZABLE) {
            // TODO: verify if there's any pending request for the same (clientId, email) combination and rate limit
            // get & check if the client is valid
            val client = getClientById(input.clientId, it)

            // check if the notification method is valid
            notificationRegistry.findOrThrow(input.notificationMethod)

            // get & check if the scopes are available
            val scopes = checkRequestedScopes(input.scope, it)

            val authRequestId = generateUUID()
            val authRequest = AuthRequestHelper(
                authRequestId,
                generateSecretId(),
                input.clientId,
                client.clientName,
                userAgent,
                input.notificationMethod,
                input.email
            )

            it.createUpdate(AuthData.INSERT_AUTH_REQUEST)
                .bindKotlin(authRequest)
                .execute()

            val batch = it.prepareBatch(AuthData.INSERT_REQUEST_SCOPES)
            scopes.map { s -> AuthRequestScope(authRequestId, s) }
                .forEach { ars ->
                    batch.bindKotlin(ars)
                        .add()
                }

            batch.execute()

            runBlocking {
                val methodSolver = methodRegistry[input.type]
                if (!methodSolver.create) {
                    it.createQuery(UserData.GET_USER_BY_EMAIL)
                        .bind(UserData.EMAIL, input.email)
                        .mapTo<User>()
                        .findOne()
                        .toNullable() ?: throw InvalidUserCreationMethodException()
                }

                methodSolver.solve(authRequest)
            }

            AuthRequestAcknowledgement(
                authRequestId,
                authRequest.expiresIn
            )
        }

    override fun verifyAuthRequest(authRequestId: String, secretId: String) =
        tm.run {
            val authReq = getAuthRequestAndVerifySecret(authRequestId, secretId, it)
            if (authReq.verified)
                throw RequestAlreadyValidatedException()

            if (authReq.hasExpired()) {
                removeAuthRequest(authRequestId, it)
                throw RequestTokenExpiredException()
            }

            it.createUpdate(AuthData.VERIFY_AUTH_REQUEST)
                .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
                .execute()

            Unit
        }

    override fun checkAuthRequest(authRequestId: String) =
        tm.run(TransactionIsolationLevel.SERIALIZABLE) {
            val authReq = getAuthRequest(authRequestId, it)
            if (authReq.verified) {
                removeAuthRequest(authRequestId, it)

                val user = getOrCreateUser(authReq.email, it)
                val userToken = createUserToken(user, authReq, it)
                val idToken = generateIdToken(user, authReq)

                val duration = Duration.between(Instant.now(), userToken.accessTokenExpires)
                AuthSuccessfulResponse(
                    userToken.accessToken,
                    "Bearer",
                    userToken.refreshToken,
                    duration.seconds,
                    idToken
                )
            } else {
                if (authReq.hasExpired()) {
                    // the request has expired
                    removeAuthRequest(authRequestId, it)
                    throw RequestTokenExpiredException()
                }

                throw RequestTokenPendingException()
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
            throw InvalidScopesException(invalidScopes)

        return scopeList
    }

    private fun getClientById(clientId: String, handle: Handle): AuthClient {
        return handle.createQuery(AuthData.GET_CLIENT_BY_ID)
            .bind(AuthData.CLIENT_ID, clientId)
            .mapTo<AuthClient>()
            .findOne()
            .toNullable() ?: throw InvalidClientIdException(clientId)
    }

    private fun getAuthRequest(authRequestId: String, handle: Handle): AuthRequest {
        return handle.createQuery(AuthData.GET_AUTH_REQUEST)
            .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
            .mapTo<AuthRequest>()
            .findOne()
            .toNullable() ?: throw RequestTokenInvalidRequestException()
    }

    private fun getAuthRequestAndVerifySecret(authRequestId: String, secretId: String, handle: Handle): AuthRequest {
        val authReq = getAuthRequest(authRequestId, handle)
        if (authReq.secretId != secretId) {
            // if the secret is wrong a malicious user is probably trying to compromise
            // this user account, so we remove the auth request and report it
            removeAuthRequest(authRequestId, handle)
            throw RequestTokenInvalidRequestException()
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

    private fun createUserToken(user: User, authReq: AuthRequest, handle: Handle): UserToken {
        // TODO: verify how many tokens this user has for this client
        val accessToken = generateSecretId()
        val refreshToken = generateSecretId()
        val accessTokenDuration = Duration.ofDays(7)
        val accessTokenExpiration = Instant.now().plus(accessTokenDuration)

        val userToken = UserToken(
            user.userId,
            authReq.clientId,
            accessToken,
            refreshToken,
            accessTokenExpiration
        )

        val tokenId = handle.createUpdate(UserData.INSERT_USER_TOKEN)
            .bindKotlin(userToken)
            .executeAndReturnGeneratedKeys(UserData.ID)
            .mapTo<Int>()
            .first()

        val scopes = getRequestScopes(authReq.authRequestId, handle)
        val batch = handle.prepareBatch(UserData.INSERT_USER_TOKEN_SCOPE)

        scopes.forEach {
            batch.bindKotlin(UserTokenScope(tokenId, it.scope))
                .add()
        }

        batch.execute()
        return userToken
    }

    private fun generateIdToken(user: User, authReq: AuthRequest): String {
        val issuedAt = Instant.now()
        val jwtDuration = Duration.ofHours(1)
        val expirationDate = Date.from(issuedAt.plus(jwtDuration))

        return Jwts.builder()
            .setExpiration(expirationDate)
            .setAudience(authReq.clientId)
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

    private fun generateSecretId(): String {
        val bytes = ByteArray(64)

        val random = SecureRandom()
        random.nextBytes(bytes)

        return base64Encoder.encodeToString(bytes)
    }
}
