package org.ionproject.core.user.auth

import org.ionproject.core.user.auth.model.AuthMethodInput
import org.springframework.stereotype.Service

@Service
class UserAuthService(val registry: AuthMethodRegistry) {

    fun getAuthMethods(): Iterable<AuthMethod> = registry.authMethods

    fun selectAuthMethod(input: AuthMethodInput) {
        registry[input.type]
            .solve(input.data)
    }

}