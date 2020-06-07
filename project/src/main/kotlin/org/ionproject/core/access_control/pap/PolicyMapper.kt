package org.ionproject.core.access_control.pap

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class PolicyMapper : RowMapper<Policy> {
  override fun map(rs: ResultSet, ctx: StatementContext?): Policy {
    return Policy(rs.getInt("scope_id"), rs.getString("method"), rs.getString("version"), rs.getString("path"))
  }

}