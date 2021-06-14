package org.ionproject.core.userApi.auth.registry

import org.ionproject.core.userApi.auth.AuthRequestInvalidNotificationMethodException

class AuthNotificationRegistry {

    private val registry = mutableSetOf<String>()

    fun register(method: String) {
        registry.add(method.toLowerCase())
    }

    fun findOrThrow(method: String) =
        registry.find { it == method.toLowerCase() }
            ?: throw AuthRequestInvalidNotificationMethodException(registry)
}
