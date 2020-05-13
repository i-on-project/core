package org.ionproject.core.programme.sql

import org.ionproject.core.programme.model.Programme
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class ProgrammeMapper : RowMapper<Programme> {
  /**
   * OfferList is not always needed, in such cases it uses an empty list,
   * in others the same method fills the list that was initialized here accordingly.
   */
  override fun map(rs: ResultSet, ctx: StatementContext?): Programme {
    return Programme(
      rs.getInt("id"),
      rs.getString("name"),
      rs.getString("acronym"),
      rs.getInt("termSize"),
      mutableListOf()
    )
  }

}