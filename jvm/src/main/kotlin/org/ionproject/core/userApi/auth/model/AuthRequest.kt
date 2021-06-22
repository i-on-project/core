package org.ionproject.core.userApi.auth.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.net.URI
import java.time.Duration
import java.time.Instant

private val AUTH_DEFAULT_EXPIRATION = Duration.ofMinutes(5).toSeconds()

data class AuthRequestHelper(
    val authRequestId: String,
    val secretId: String,
    val client: AuthClient,
    val userAgent: String,
    val loginHint: String,
    val time: Instant = Instant.now(),
    val expiresIn: Long = AUTH_DEFAULT_EXPIRATION,
    val expiration: Instant = time.plusSeconds(expiresIn)
)

data class AuthRequestInput(
    val scope: String,
    @JsonProperty("acr_values")
    val acrValues: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String?,
    @JsonProperty("login_hint")
    val loginHint: String
)

data class AuthTokenInput(
    @JsonProperty("grant_type")
    val grantType: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String?,
    @JsonProperty("auth_req_id")
    val authRequestId: String?,
    @JsonProperty("refresh_token")
    val refreshToken: String?
)

data class AuthRequest(
    @ColumnName("auth_req_id")
    val authRequestId: String,
    @ColumnName("secret_id")
    val secretId: String,
    @ColumnName("login_hint")
    val loginHint: String,
    @ColumnName("user_agent")
    val userAgent: String,
    @ColumnName("client_id")
    val clientId: String,
    @ColumnName("expires_on")
    val expiresOn: Instant,
    val verified: Boolean
)

data class AuthRequestOutput(
    @JsonProperty("auth_req_id")
    val authRequestId: String,
    @JsonProperty("expires_on")
    val expiresOn: Instant,
    val client: AuthClientOutput,
    val scopes: List<AuthScope>,
    @JsonProperty("verify_action")
    val verifyAction: URI
)
