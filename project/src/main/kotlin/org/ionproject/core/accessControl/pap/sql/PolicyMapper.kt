package org.ionproject.core.accessControl.pap.sql

import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class PolicyMapper : RowMapper<PolicyEntity> {
  override fun map(rs: ResultSet, ctx: StatementContext?): PolicyEntity {
    return PolicyEntity(rs.getInt("scope_id"), rs.getString("method"), rs.getString("version"), rs.getString("path"))
  }

}