package org.ionproject.core.access_control.pap

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class TokenMapper : RowMapper<TokenEntity> {
  override fun map(rs: ResultSet, ctx: StatementContext?): TokenEntity {
    val jsonClaims = rs.getString("claims")
    return TokenEntity(rs.getString("hash"), rs.getBoolean("isValid"), deserialize(jsonClaims))
  }

  private fun deserialize(claims: String): Claims {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(claims, Claims::class.java)
  }

}