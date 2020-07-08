package org.ionproject.core.accessControl.pap.entities

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class TokenEntity(
    val hash: String,
    val isValid: Boolean,
    val issuedAt: Long,
    val expiresAt: Long,
    val derivedToken: Boolean,
    val claims: ClaimsEntity
)

interface ClaimsEntity

class TokenClaims(
    val scope: String
) : ClaimsEntity

class DerivedTokenClaims(
    val fatherTokenHash : String,
    val derivedTokenReference: String,
    val uri: String
) : ClaimsEntity