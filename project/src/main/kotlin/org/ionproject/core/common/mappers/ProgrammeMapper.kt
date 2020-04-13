package org.ionproject.core.common.mappers

import org.ionproject.core.common.model.Programme
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ProgrammeMapper : RowMapper<Programme> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): Programme {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}