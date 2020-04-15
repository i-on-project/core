package org.ionproject.core.common.mappers

import org.ionproject.core.klass.Klass
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ClassMapper : RowMapper<Klass> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Klass {
        /*
         * Missing calendar Id
         */
        return Klass(rs.getInt("courseId"), null, rs.getString("term"))
    }

}