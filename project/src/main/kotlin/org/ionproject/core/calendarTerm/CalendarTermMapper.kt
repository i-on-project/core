package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.OffsetDateTime

@Component
class CalendarTermMapper : RowMapper<CalendarTerm> {
    override fun map(rs: ResultSet, ctx: StatementContext?): CalendarTerm {
        /*
         *https://jdbc.postgresql.org/documentation/head/8-date-time.html
         * OffsetDateTime, correspondent of timestamp with timezone
         */
        return CalendarTerm(
            rs.getString("id"),
            rs.getObject("start_date", OffsetDateTime::class.java),
            rs.getObject("end_date", OffsetDateTime::class.java)
        )
    }
}