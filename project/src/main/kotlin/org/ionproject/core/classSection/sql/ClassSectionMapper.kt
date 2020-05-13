package org.ionproject.core.classSection.sql

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.classSection.sql.ClassSectionData.ACR
import org.ionproject.core.classSection.sql.ClassSectionData.CAL_TERM
import org.ionproject.core.classSection.sql.ClassSectionData.CID
import org.ionproject.core.classSection.sql.ClassSectionData.ID
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class ClassSectionMapper : RowMapper<ClassSection> {
  override fun map(rs: ResultSet, ctx: StatementContext?): ClassSection {
    return ClassSection(
      rs.getInt(CID),
      rs.getString(ACR),
      rs.getString(CAL_TERM),
      rs.getString(ID))
  }
}
