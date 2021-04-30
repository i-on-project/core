package org.ionproject.core.user.auth

import org.ionproject.core.common.Uri
import org.ionproject.core.user.auth.model.AuthMethodInput
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserAuthController(val service: UserAuthService) {

    @GetMapping(Uri.authMethods, produces = ["application/json"])
    fun getAvailableMethods(): ResponseEntity<Iterable<AuthMethod>> {
        return ResponseEntity.ok(service.getAuthMethods())
    }

    @PostMapping(Uri.authMethods, produces = ["application/json"])
    fun selectAuthMethod(
        @RequestBody methodInput: AuthMethodInput
    ): ResponseEntity<Unit> {
        service.selectAuthMethod(methodInput)
        return ResponseEntity.ok(Unit)
    }

    @PostMapping(Uri.authPoll)
    fun pollForUserAuth() {
    }
}
