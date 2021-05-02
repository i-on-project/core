package org.ionproject.core.user.auth.model

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthMethodInput(
    val scope: String,
    val type: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("notification_method")
    val notificationMethod: String,
    @JsonProperty("login_hint")
    val loginHint: String?
)