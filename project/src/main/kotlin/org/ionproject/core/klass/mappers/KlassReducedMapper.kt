package org.ionproject.core.klass.mappers


import org.ionproject.core.klass.model.Klass
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class KlassReducedMapper : RowMapper<Klass> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Klass {
        /*
         * Missing calendar Id
         */
        return Klass(rs.getInt("courseId"), null, rs.getString("term"))
    }
}