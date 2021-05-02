package org.ionproject.core.user.auth.model

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class AuthClient(
    @ColumnName("client_id")
    val clientId: String,
    @ColumnName("client_name")
    val clientName: String,
    @ColumnName("client_url")
    val clientUrl: String
)