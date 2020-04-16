package org.ionproject.core.common.mappers

import org.ionproject.core.common.model.ProgrammeOffer
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

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