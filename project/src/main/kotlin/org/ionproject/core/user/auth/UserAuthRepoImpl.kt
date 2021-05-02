package org.ionproject.core.user.auth

import kotlinx.coroutines.runBlocking
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.toNullable
import org.ionproject.core.user.auth.model.AuthClient
import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthRequest
import org.ionproject.core.user.auth.model.AuthRequestHelper
import org.ionproject.core.user.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.user.auth.model.AuthSuccessfulResponse
import org.ionproject.core.user.auth.sql.AuthData
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UserAuthRepoImpl(
    val tm: TransactionManager,
    val methodRegistry: AuthMethodRegistry,
    val notificationRegistry: AuthNotificationRegistry
) : UserAuthRepo {

    override fun getAuthMethods() = methodRegistry.authMethods

    override fun addAuthRequest(userAgent: String, input: AuthMethodInput) =
        tm.run(TransactionIsolationLevel.SERIALIZABLE) {
            val client = getClientById(input.clientId, it)

            notificationRegistry.findOrThrow(input.notificationMethod)

            val authRequestId = generateAuthRequestId()
            val authRequest = AuthRequestHelper(
                authRequestId,
                input.clientId,
                client.clientName,
                userAgent,
                input.notificationMethod,
                input.loginHint
            )

            it.createUpdate(AuthData.INSERT_AUTH_REQUEST)
                .bind(AuthData.AUTH_REQUEST_ID, authRequest.authRequestId)
                .bind(AuthData.LOGIN_HINT, authRequest.loginHint)
                .bind(AuthData.USER_AGENT, authRequest.userAgent)
                .bind(AuthData.CLIENT_ID, authRequest.clientId)
                .bind(AuthData.NOTIFICATION_METHOD, authRequest.notificationMethod)
                .bind(AuthData.EXPIRES_ON, authRequest.expiration)
                .execute()

            runBlocking {
                methodRegistry[input.type].solve(authRequest)
            }

            AuthRequestAcknowledgement(
                authRequestId,
                authRequest.expiresIn
            )
        }

    override fun validateAuthRequest(authRequestId: String) =
        tm.run {
            // TODO: improve this
            val authReq = getAuthRequest(authRequestId, it)
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

                // TODO: change this later: create user and necessary tokens
                AuthSuccessfulResponse(
                    "dummy",
                    "Bearer",
                    "dummy",
                    0,
                    "dummy"
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

    private fun removeAuthRequest(authRequestId: String, handle: Handle) {
        handle.createUpdate(AuthData.REMOVE_AUTH_REQUEST)
            .bind(AuthData.AUTH_REQUEST_ID, authRequestId)
            .execute()
    }

    private fun AuthRequest.hasExpired() = expiresOn.isBefore(Instant.now())

    private fun generateAuthRequestId() = UUID.randomUUID().toString()

}