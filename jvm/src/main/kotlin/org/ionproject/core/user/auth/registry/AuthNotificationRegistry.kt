package org.ionproject.core.user.auth.registry

import org.ionproject.core.user.auth.InvalidNotificationMethodException

class AuthNotificationRegistry {

    private val registry = mutableSetOf<String>()

    fun register(method: String) {
        registry.add(method.toLowerCase())
    }

    fun findOrThrow(method: String) =
        registry.find { it == method.toLowerCase() }
            ?: throw InvalidNotificationMethodException(registry)
}
