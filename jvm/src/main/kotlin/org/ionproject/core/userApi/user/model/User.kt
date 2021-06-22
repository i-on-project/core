package org.ionproject.core.userApi.user.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.Instant

data class User(
    @ColumnName("user_id")
    val userId: String,
    val email: String,
    @ColumnName("created_at")
    val createdAt: Instant = Instant.now(),
    val name: String? = null
)

data class UserEditInput(
    val name: String
)

data class UserToken(
    @ColumnName("token_id")
    val tokenId: Int,
    @ColumnName("user_id")
    val userId: String,
    @ColumnName("client_id")
    val clientId: String,
    @ColumnName("access_token")
    val accessToken: String,
    @ColumnName("refresh_token")
    val refreshToken: String,
    @ColumnName("at_expires")
    val accessTokenExpires: Instant,
    @ColumnName("created_at")
    val createdAt: Instant = Instant.now(),
    @ColumnName("updated_at")
    val updatedAt: Instant = Instant.now()
)

data class UserTokenScope(
    @ColumnName("token_id")
    val tokenId: Int,
    @ColumnName("scope_id")
    val scopeId: String
)

data class UserRevokeTokenInput(
    @JsonProperty("token")
    val accessToken: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String?
)

data class UserTokenInfo(
    val token: UserToken,
    val scopes: Set<UserTokenScope>
)
