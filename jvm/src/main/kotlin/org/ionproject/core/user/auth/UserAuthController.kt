package org.ionproject.core.user.auth

import org.ionproject.core.common.Uri
import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.user.auth.model.AuthScope
import org.ionproject.core.user.auth.model.AuthSuccessfulResponse
import org.ionproject.core.user.auth.model.AuthVerification
import org.ionproject.core.user.auth.registry.AuthMethod
import org.ionproject.core.user.auth.repo.UserAuthRepo
import org.springframework.http.ResponseEntity
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

    @GetMapping(Uri.authRequestScopes, produces = ["application/json"])
    fun getAuthRequestScopes(
        @PathVariable reqId: String
    ): ResponseEntity<Iterable<AuthScope>> {
        return ResponseEntity.ok(repo.getRequestScopes(reqId))
    }

    @PostMapping(Uri.authVerify)
    fun verifyUserAuth(
        @RequestBody verification: AuthVerification
    ): ResponseEntity<Unit> {
        repo.verifyAuthRequest(verification.authReqId, verification.secret)
        return ResponseEntity.noContent()
            .build()
    }

    @GetMapping(Uri.authPoll, produces = ["application/json"])
    fun pollForUserAuth(
        @PathVariable reqId: String
    ): ResponseEntity<AuthSuccessfulResponse> {
        return ResponseEntity.ok(repo.checkAuthRequest(reqId))
    }
}
