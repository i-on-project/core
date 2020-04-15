package org.ionproject.core.common.mappers

import org.ionproject.core.common.model.Programme
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ProgrammeMapper : RowMapper<Programme> {
    /**
     *OfferList is not always needed, in such cases it uses an empty list,
     * in others the same method fills the list that was initialized here accordingly.
     */
    override fun map(rs: ResultSet, ctx: StatementContext?): Programme {
        return Programme(
            rs.getInt("id"), rs.getString("acronym"),
            rs.getString("name"), rs.getInt("termSize"), mutableListOf()
        )
    }

}