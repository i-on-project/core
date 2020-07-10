package org.ionproject.core.accessControl.pap.entities

class TokenEntity(
    val hash: String,
    val isValid: Boolean,
    val issuedAt: Long,
    val expiresAt: Long,
    val derivedToken: Boolean,
    val fatherHash: String,
    val claims: ClaimsEntity
)

interface ClaimsEntity

class TokenClaims(
    val scope: String
) : ClaimsEntity

class DerivedTokenClaims(
    val scope: String,
    val derivedTokenReference: String
) : ClaimsEntity
