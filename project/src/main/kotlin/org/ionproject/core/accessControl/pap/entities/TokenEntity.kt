package org.ionproject.core.accessControl.pap.entities

class TokenEntity(
    val hash: String,
    val isValid: Boolean,
    val issuedAt: Long,
    val expiresAt: Long,
    val claims: ClaimsEntity
)