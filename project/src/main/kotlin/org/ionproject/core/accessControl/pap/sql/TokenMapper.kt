package org.ionproject.core.accessControl.pap.sql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.entities.DerivedTokenClaims
import org.ionproject.core.accessControl.pap.entities.TokenClaims
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class TokenMapper() : RowMapper<TokenEntity> {
    override fun map(rs: ResultSet, ctx: StatementContext?): TokenEntity {
        return buildTokenEntity(rs, deserializeToken(rs.getString("claims")))
    }

    private fun deserializeToken(claims: String): TokenClaims {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(claims, TokenClaims::class.java)
    }

}

class DerivedTokenMapper() : RowMapper<TokenEntity> {
    override fun map(rs: ResultSet, ctx: StatementContext?): TokenEntity {
        return buildTokenEntity(rs, deserializeDerived(rs.getString("claims")))
    }

    private fun deserializeDerived(claims: String): DerivedTokenClaims {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(claims, DerivedTokenClaims::class.java)
    }

}

fun buildTokenEntity(rs: ResultSet, claims: ClaimsEntity) : TokenEntity {
    return TokenEntity(
        rs.getString("hash"),
        rs.getBoolean("isValid"),
        rs.getLong("issuedAt"),
        rs.getLong("expiresAt"),
        rs.getBoolean("derivedToken"),
        claims
    )
}