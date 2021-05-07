package org.ionproject.core.user.auth.repo

import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.user.auth.model.AuthRequestOutput
import org.ionproject.core.user.auth.model.AuthSuccessfulResponse
import org.ionproject.core.user.auth.registry.AuthMethod

interface UserAuthRepo {

    fun getAuthMethods(): Iterable<AuthMethod>

    fun addAuthRequest(userAgent: String, input: AuthMethodInput): AuthRequestAcknowledgement

    fun verifyAuthRequest(authRequestId: String, secretId: String)

    fun checkAuthRequest(authRequestId: String): AuthSuccessfulResponse

    fun getAuthRequest(authRequestId: String): AuthRequestOutput
}
