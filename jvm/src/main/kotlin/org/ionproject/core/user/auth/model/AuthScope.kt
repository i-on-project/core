package org.ionproject.core.user.auth.model

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class AuthScope(
    @ColumnName("scope_id")
    val scope: String,
    @ColumnName("scope_name")
    val scopeName: String,
    @ColumnName("scope_description")
    val scopeDescription: String
)

data class AuthRequestScope(
    @ColumnName("auth_req_id")
    val authRequestId: String,
    @ColumnName("scope_id")
    val scope: String
)
