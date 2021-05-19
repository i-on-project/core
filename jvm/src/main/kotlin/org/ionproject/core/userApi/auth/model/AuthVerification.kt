package org.ionproject.core.userApi.auth.model

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthVerification(
    @JsonProperty("auth_req_id")
    val authReqId: String,
    val secret: String
)
