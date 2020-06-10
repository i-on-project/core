package org.ionproject.core.accessControl.pap.sql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class TokenMapper : RowMapper<TokenEntity> {
  override fun map(rs: ResultSet, ctx: StatementContext?): TokenEntity {
    val jsonClaims = rs.getString("claims")
    return TokenEntity(rs.getString("hash"), rs.getBoolean("isValid"), rs.getLong("issuedAt"), rs.getLong("expiresAt"), deserialize(jsonClaims))
  }

  private fun deserialize(claims: String): ClaimsEntity {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(claims, ClaimsEntity::class.java)
  }

}