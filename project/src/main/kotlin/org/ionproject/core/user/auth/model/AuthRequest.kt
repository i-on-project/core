package org.ionproject.core.user.auth.model

import java.time.Instant

private const val AUTH_DEFAULT_EXPIRATION = 5L * 60 // 5 minutes

data class AuthRequest(
    val authRequestId: String,
    val clientId: String,
    val userAgent: String,
    val notificationMethod: String,
    val loginHint: String? = null,
    val time: Instant = Instant.now(),
    val expiresIn: Long = AUTH_DEFAULT_EXPIRATION,
    val expiration: Instant = time.plusSeconds(expiresIn)
)
