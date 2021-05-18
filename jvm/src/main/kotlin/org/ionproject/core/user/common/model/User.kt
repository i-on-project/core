package org.ionproject.core.user.common.model

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

data class UserToken(
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
    val updatedAt: Instant = Instant.now(),
    val id: Int? = null
)

data class UserTokenScope(
    val id: Int,
    @ColumnName("scope_id")
    val scope: String
)

data class UserTokenInput(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String
)
