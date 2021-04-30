package org.ionproject.core.user.auth

import org.ionproject.core.common.Uri
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserAuthController {

    @GetMapping(Uri.authMethods)
    fun getAvailableMethods() {

    }

    @PostMapping(Uri.authMethods)
    fun selectAuthMethod() {

    }

    @PostMapping(Uri.authPoll)
    fun pollForUserAuth() {

    }

}