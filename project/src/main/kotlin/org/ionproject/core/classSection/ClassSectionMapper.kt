package org.ionproject.core.classSection

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class ClassSectionMapper : RowMapper<ClassSection> {
    override fun map(rs: ResultSet, ctx: StatementContext?): ClassSection {
        return ClassSection(rs.getInt("cid"), rs.getString("acronym"), rs.getString("term"), rs.getString("sid"))
    }
}
