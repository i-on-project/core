package org.ionproject.core.userApi.auth.model

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthMethodInput(
    val scope: String,
    val type: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("notification_method")
    val notificationMethod: String,
    @JsonProperty("email")
    val email: String
)
