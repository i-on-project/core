package org.ionproject.core.klass.sql

import org.ionproject.core.klass.model.Klass
import org.ionproject.core.klass.sql.KlassData.CAL_TERM
import org.ionproject.core.klass.sql.KlassData.CID
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class KlassReducedMapper : RowMapper<Klass> {
  override fun map(rs: ResultSet, ctx: StatementContext?): Klass {
    return Klass(
      rs.getInt(CID),
      null,
      rs.getString(CAL_TERM))
  }
}