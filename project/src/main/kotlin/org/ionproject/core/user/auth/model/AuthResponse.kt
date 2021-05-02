package org.ionproject.core.user.auth.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

enum class AuthError {
    @JsonProperty("invalid_request")
    INVALID_REQUEST,
    @JsonProperty("invalid_scope")
    INVALID_SCOPE
}

data class AuthErrorResponse(
    val error: AuthError,
    @JsonProperty("error_description")
    val errorDescription: String? = null,
    @JsonProperty("error_uri")
    val errorUri: URI? = null
)

enum class TokenError {
    @JsonProperty("invalid_request")
    INVALID_REQUEST,
    @JsonProperty("authorization_pending")
    AUTHORIZATION_PENDING,
    @JsonProperty("slow_down")
    SLOW_DOWN,
    @JsonProperty("expired_token")
    EXPIRED_TOKEN,
    @JsonProperty("access_denied")
    ACCESS_DENIED
}

data class AuthTokenError(
    val error: TokenError
)

data class AuthRequestAcknowledgement(
    @JsonProperty("auth_req_id")
    val authRequestId: String,
    @JsonProperty("expires_in")
    val expiresIn: Long,
    val interval: Int? = null
)

data class AuthSuccessfulResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    @JsonProperty("id_token")
    val idToken: String
)
