package org.ionproject.core.user.auth

import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.user.auth.model.AuthSuccessfulResponse

interface UserAuthRepo {

    fun getAuthMethods(): Iterable<AuthMethod>

    fun addAuthRequest(userAgent: String, input: AuthMethodInput): AuthRequestAcknowledgement

    fun validateAuthRequest(authRequestId: String)

    fun checkAuthRequest(authRequestId: String): AuthSuccessfulResponse

}