package org.ionproject.core.access_control.pap

import org.ionproject.core.access_control.pap.TokenEntity
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class TokenMapper : RowMapper<TokenEntity> {
  override fun map(rs: ResultSet, ctx: StatementContext?): TokenEntity {
    val jsonClaims = rs.getString("claims")
    //TODO(Implement JSON converter)
    return TokenEntity(rs.getString("hash"), rs.getBoolean("isValid"), LinkedHashMap())
  }

}