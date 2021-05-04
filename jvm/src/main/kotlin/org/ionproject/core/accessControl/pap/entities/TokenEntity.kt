package org.ionproject.core.accessControl.pap.entities

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

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
) : ClaimsEntity {
    companion object {
        fun deserialize(claims: String): TokenClaims {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(claims, TokenClaims::class.java)
        }
    }
}

class DerivedTokenClaims(
    val scope: String,
    val derivedTokenReference: String
) : ClaimsEntity {
    companion object {
        fun deserialize(claims: String): DerivedTokenClaims {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(claims, DerivedTokenClaims::class.java)
        }
    }
}
