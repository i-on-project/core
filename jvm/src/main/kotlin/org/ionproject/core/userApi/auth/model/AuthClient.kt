package org.ionproject.core.userApi.auth.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class AuthClient(
    @ColumnName("client_id")
    val clientId: String,
    @ColumnName("client_secret")
    val clientSecret: String?,
    @ColumnName("client_name")
    val clientName: String,
    @ColumnName("client_url")
    val clientUrl: String?
) {
    fun toOutput() = AuthClientOutput(clientName, clientUrl, clientSecret != null)
}

data class AuthClientOutput(
    @JsonProperty("client_name")
    val clientName: String,
    @JsonProperty("client_url")
    val clientUrl: String?,
    val confidential: Boolean
)
