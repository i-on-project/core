package org.ionproject.core.accessControl.pap.sql

import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class TokenMapper(private val deserialize: (String) -> ClaimsEntity) : RowMapper<TokenEntity> {
    override fun map(rs: ResultSet, ctx: StatementContext?): TokenEntity {
        val claims = rs.getString("claims")

        return TokenEntity(
            rs.getString("hash"),
            rs.getBoolean("isValid"),
            rs.getLong("issuedAt"),
            rs.getLong("expiresAt"),
            rs.getBoolean("derivedToken"),
            rs.getString("fatherHash"),
            deserialize(claims)
        )
    }
}
