package org.ionproject.core.user.auth.model

data class AuthMethodInput(
    val type: String,
    val data: String?
)