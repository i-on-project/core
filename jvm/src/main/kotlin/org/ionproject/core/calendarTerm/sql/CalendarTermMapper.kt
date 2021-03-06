package org.ionproject.core.calendarTerm.sql

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.calendarTerm.sql.CalendarTermData.END_DATE
import org.ionproject.core.calendarTerm.sql.CalendarTermData.START_DATE
import org.ionproject.core.course.sql.CourseData.ID
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime

@Component
class CalendarTermMapper : RowMapper<CalendarTerm> {
    override fun map(rs: ResultSet, ctx: StatementContext?): CalendarTerm {
        /*
         * https://jdbc.postgresql.org/documentation/head/8-date-time.html
         * OffsetDateTime, correspondent of timestamp with timezone
         */
        return CalendarTerm(
            rs.getString(ID),
            rs.getObject(START_DATE, LocalDateTime::class.java),
            rs.getObject(END_DATE, LocalDateTime::class.java)
        )
    }
}
