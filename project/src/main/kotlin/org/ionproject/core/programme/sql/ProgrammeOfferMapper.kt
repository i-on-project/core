package org.ionproject.core.programme.sql

import org.ionproject.core.programme.model.ProgrammeOffer
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class ProgrammeOfferMapper : RowMapper<ProgrammeOffer> {
  override fun map(rs: ResultSet, ctx: StatementContext?): ProgrammeOffer {
    return ProgrammeOffer(
      rs.getInt("id"),
      rs.getString("courseAcr"),
      rs.getInt("programmeId"),
      rs.getInt("courseId"),
      rs.getInt("termNumber"),
      rs.getBoolean("optional")
    )
  }

}