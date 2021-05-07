package org.ionproject.core.user.auth.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class AuthClient(
    @ColumnName("client_id")
    @JsonProperty("client_id")
    val clientId: String,
    @ColumnName("client_name")
    @JsonProperty("client_name")
    val clientName: String,
    @ColumnName("client_url")
    @JsonProperty("client_url")
    val clientUrl: String
)
