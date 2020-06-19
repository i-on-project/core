package org.ionproject.core.readApi.klass.sql

import org.ionproject.core.readApi.classSection.ClassSection
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class KlassClassSectionMapper : RowMapper<ClassSection> {
    override fun map(rs: ResultSet, ctx: StatementContext?): ClassSection {
        return ClassSection(
            rs.getInt(KlassData.CID),
            rs.getString(KlassData.ACR),
            rs.getString(KlassData.CAL_TERM),
            rs.getString(KlassData.SID))
    }
}
