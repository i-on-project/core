package org.ionproject.core.userApi.auth

import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.auth.model.AuthMethodInput
import org.ionproject.core.userApi.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.userApi.auth.model.AuthRequestOutput
import org.ionproject.core.userApi.auth.model.AuthSuccessfulResponse
import org.ionproject.core.userApi.auth.model.AuthVerification
import org.ionproject.core.userApi.auth.registry.AuthMethod
import org.ionproject.core.userApi.auth.repo.UserAuthRepo
import org.ionproject.core.userApi.user.model.UserTokenInput
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

@RestController
class UserAuthController(val repo: UserAuthRepo) {

    @GetMapping(Uri.authMethods, produces = ["application/json"])
    fun getAvailableMethods(): ResponseEntity<Iterable<AuthMethod>> {
        return ResponseEntity.ok(repo.getAuthMethods())
    }

    @PostMapping(Uri.authMethods, produces = ["application/json"])
    fun selectAuthMethod(
        webRequest: WebRequest,
        @RequestBody methodInput: AuthMethodInput
    ): ResponseEntity<AuthRequestAcknowledgement> {
        val userAgent = webRequest.getHeader("User-Agent") ?: "Unknown"
        return ResponseEntity.ok(repo.addAuthRequest(userAgent, methodInput))
    }

    @GetMapping(Uri.authRequestBase, produces = ["application/json"])
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

    @GetMapping(Uri.authPoll, produces = ["application/json"])
    fun pollForUserAuth(
        @PathVariable reqId: String
    ): ResponseEntity<AuthSuccessfulResponse> {
        return ResponseEntity.ok(repo.checkAuthRequest(reqId))
    }

    @PostMapping(Uri.authRefreshToken, produces = ["application/json"])
    fun refreshAccessToken(
        @RequestBody input: UserTokenInput
    ): ResponseEntity<AuthSuccessfulResponse> {
        return ResponseEntity.ok(repo.refreshAccessToken(input.accessToken, input.refreshToken))
    }

    @DeleteMapping(Uri.authRevokeToken)
    fun revokeAccessToken(
        @RequestBody input: UserTokenInput
    ): ResponseEntity<Unit> {
        repo.revokeAccessToken(input.accessToken, input.refreshToken)
        return ResponseEntity.noContent().build()
    }
}
