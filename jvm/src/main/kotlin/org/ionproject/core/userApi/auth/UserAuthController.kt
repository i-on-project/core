package org.ionproject.core.userApi.auth

import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.userApi.auth.model.AuthRequestInput
import org.ionproject.core.userApi.auth.model.AuthRequestOutput
import org.ionproject.core.userApi.auth.model.AuthSuccessfulResponse
import org.ionproject.core.userApi.auth.model.AuthTokenInput
import org.ionproject.core.userApi.auth.model.AuthVerification
import org.ionproject.core.userApi.auth.registry.AuthMethod
import org.ionproject.core.userApi.auth.repo.UserAuthRepo
import org.ionproject.core.userApi.user.model.UserRevokeTokenInput
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

private const val CIBA_GRANT_TYPE = "urn:openid:params:grant-type:ciba"
private const val REFRESH_GRANT_TYPE = "refresh_token"

@RestController
class UserAuthController(val repo: UserAuthRepo) {

    @GetMapping(Uri.authMethods, produces = ["application/json"])
    fun getAvailableMethods(): ResponseEntity<Iterable<AuthMethod>> {
        return ResponseEntity.ok(repo.getAuthMethods())
    }

    @PostMapping(Uri.authBackchannel, produces = ["application/json"])
    fun initiateBackChannelAuth(
        webRequest: WebRequest,
        @RequestBody requestInput: AuthRequestInput
    ): ResponseEntity<AuthRequestAcknowledgement> {
        val userAgent = webRequest.getHeader("User-Agent") ?: "Unknown"
        return ResponseEntity.ok(repo.addAuthRequest(userAgent, requestInput))
    }

    @GetMapping(Uri.authRequest, produces = ["application/json"])
    fun getAuthRequest(
        @PathVariable reqId: String
    ): ResponseEntity<AuthRequestOutput> {
        return ResponseEntity.ok(repo.getAuthRequest(reqId))
    }

    @PostMapping(Uri.authVerify)
    fun verifyUserAuth(
        @RequestBody verification: AuthVerification
    ): ResponseEntity<Unit> {
        repo.verifyAuthRequest(verification.authReqId, verification.secret)
        return ResponseEntity.noContent().build()
    }

    @PostMapping(Uri.authToken, produces = ["application/json"])
    fun handleTokenEndpoint(
        @RequestBody tokenBody: AuthTokenInput
    ): ResponseEntity<AuthSuccessfulResponse> {
        // verify grant type
        if (tokenBody.grantType == CIBA_GRANT_TYPE) {
            return ResponseEntity.ok(repo.checkAuthRequest(tokenBody))
        } else if (tokenBody.grantType == REFRESH_GRANT_TYPE) {
            return ResponseEntity.ok(repo.refreshAccessToken(tokenBody))
        }

        throw UnsupportedGrantTypeException()
    }

    @DeleteMapping(Uri.authRevokeToken)
    fun revokeAccessToken(
        @RequestBody input: UserRevokeTokenInput
    ): ResponseEntity<Unit> {
        repo.revokeAccessToken(input)
        return ResponseEntity.noContent().build()
    }
}
