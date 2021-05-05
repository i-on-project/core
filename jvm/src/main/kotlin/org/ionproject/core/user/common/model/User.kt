package org.ionproject.core.user.common.model

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
    @ColumnName("id_token")
    val idToken: String,
    @ColumnName("at_expires")
    val accessTokenExpires: Instant,
    @ColumnName("created_at")
    val createdAt: Instant
)