package org.ionproject.core.userApi.auth.repo

import org.ionproject.core.userApi.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.userApi.auth.model.AuthRequestInput
import org.ionproject.core.userApi.auth.model.AuthRequestOutput
import org.ionproject.core.userApi.auth.model.AuthSuccessfulResponse
import org.ionproject.core.userApi.auth.model.AuthTokenInput
import org.ionproject.core.userApi.auth.registry.AuthMethod
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserRevokeTokenInput
import org.ionproject.core.userApi.user.model.UserTokenInfo

interface UserAuthRepo {

    fun getAuthMethods(): Iterable<AuthMethod>

    fun addAuthRequest(userAgent: String, input: AuthRequestInput): AuthRequestAcknowledgement

    fun verifyAuthRequest(authRequestId: String, secretId: String)

    fun checkAuthRequest(tokenInput: AuthTokenInput): AuthSuccessfulResponse

    fun getAuthRequest(authRequestId: String): AuthRequestOutput

    fun getUserByToken(accessToken: String): User?

    fun getTokenInfo(accessToken: String): UserTokenInfo

    fun refreshAccessToken(tokenInput: AuthTokenInput): AuthSuccessfulResponse

    fun revokeAccessToken(revokeInput: UserRevokeTokenInput)
}
