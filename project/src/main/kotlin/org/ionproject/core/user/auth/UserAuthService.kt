package org.ionproject.core.user.auth

import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthMethodResponse
import org.ionproject.core.user.auth.model.AuthRequest
import org.springframework.stereotype.Service
import java.util.UUID

private const val POLLING_INTERVAL = 2

@Service
class UserAuthService(val registry: AuthMethodRegistry, val repo: UserAuthRepo) {

    fun getAuthMethods(): Iterable<AuthMethod> = registry.authMethods

    fun selectAuthMethod(userAgent: String, input: AuthMethodInput): AuthMethodResponse {
        val authRequestId = generateAuthRequestId()
        val authRequest = AuthRequest(
            authRequestId,
            input.clientId,
            userAgent,
            input.notificationMethod,
            input.loginHint
        )

        repo.addAuthRequest(authRequest) {
            // solve the auth method in the transaction so if it fails
            // the transaction will also fail and thus rollback
            registry[input.type].solve(authRequest)
        }

        return AuthMethodResponse(
            authRequestId,
            authRequest.expiresIn.toInt(),
            POLLING_INTERVAL
        )
    }

    private fun generateAuthRequestId() = UUID.randomUUID().toString()

}