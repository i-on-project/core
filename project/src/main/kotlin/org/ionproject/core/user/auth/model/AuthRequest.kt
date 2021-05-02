package org.ionproject.core.user.auth.model

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.Instant

private const val AUTH_DEFAULT_EXPIRATION = 5L * 60 // 5 minutes

data class AuthRequestHelper(
    val authRequestId: String,
    val clientId: String,
    val clientName: String,
    val userAgent: String,
    val notificationMethod: String,
    val loginHint: String? = null,
    val time: Instant = Instant.now(),
    val expiresIn: Long = AUTH_DEFAULT_EXPIRATION,
    val expiration: Instant = time.plusSeconds(expiresIn)
)

data class AuthRequest(
    @ColumnName("auth_req_id")
    val authRequestId: String,
    @ColumnName("login_hint")
    val loginHint: String,
    @ColumnName("user_agent")
    val userAgent: String,
    @ColumnName("client_id")
    val clientId: String,
    @ColumnName("ntf_method")
    val notificationMethod: String,
    @ColumnName("expires_on")
    val expiresOn: Instant,
    val verified: Boolean
)
