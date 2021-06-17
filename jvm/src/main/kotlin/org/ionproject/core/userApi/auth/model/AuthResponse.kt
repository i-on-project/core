package org.ionproject.core.userApi.auth.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.ionproject.core.userApi.user.model.UserToken
import java.net.URI
import java.time.Duration
import java.time.Instant
enum class AuthError {
    @JsonProperty("invalid_request")
    INVALID_REQUEST,
    @JsonProperty("authorization_pending")
    AUTHORIZATION_PENDING,
    @JsonProperty("slow_down")
    SLOW_DOWN,
    @JsonProperty("expired_token")
    EXPIRED_TOKEN,
    @JsonProperty("access_denied")
    ACCESS_DENIED,
    @JsonProperty("invalid_scope")
    INVALID_SCOPE,
    @JsonProperty("invalid_client")
    INVALID_CLIENT,
    @JsonProperty("invalid_grant")
    INVALID_GRANT,
    @JsonProperty("unauthorized_client")
    UNAUTHORIZED_CLIENT,
    @JsonProperty("unsupported_grant_type")
    UNSUPPORTED_GRANT_TYPE
}

data class AuthErrorResponse(
    val error: AuthError,
    @JsonProperty("error_description")
    val errorDescription: String? = null,
    @JsonProperty("error_uri")
    val errorUri: URI? = null
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
    val expiresIn: Long,
    @JsonProperty("id_token")
    val idToken: String
) {
    companion object {
        fun from(userToken: UserToken, idToken: String) = AuthSuccessfulResponse(
            userToken.accessToken,
            "Bearer",
            userToken.refreshToken,
            Duration.between(Instant.now(), userToken.accessTokenExpires).seconds,
            idToken
        )
    }
}
