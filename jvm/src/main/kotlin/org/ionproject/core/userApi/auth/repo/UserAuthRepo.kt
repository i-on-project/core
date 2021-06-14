package org.ionproject.core.userApi.auth.repo

import org.ionproject.core.userApi.auth.model.AuthMethodInput
import org.ionproject.core.userApi.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.userApi.auth.model.AuthRequestOutput
import org.ionproject.core.userApi.auth.model.AuthSuccessfulResponse
import org.ionproject.core.userApi.auth.registry.AuthMethod
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserTokenInfo

interface UserAuthRepo {

    fun getAuthMethods(): Iterable<AuthMethod>

    fun addAuthRequest(userAgent: String, input: AuthMethodInput): AuthRequestAcknowledgement

    fun verifyAuthRequest(authRequestId: String, secretId: String)

    fun checkAuthRequest(authRequestId: String): AuthSuccessfulResponse

    fun getAuthRequest(authRequestId: String): AuthRequestOutput

    fun getUserByToken(accessToken: String): User?

    fun getTokenInfo(accessToken: String): UserTokenInfo

    fun refreshAccessToken(accessToken: String, refreshToken: String): AuthSuccessfulResponse

    fun revokeAccessToken(accessToken: String, refreshToken: String)
}
