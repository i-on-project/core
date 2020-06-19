package org.ionproject.core.readApi.course.sql

import org.ionproject.core.readApi.course.model.Course
import org.ionproject.core.readApi.course.sql.CourseData.ACR
import org.ionproject.core.readApi.course.sql.CourseData.CAL_TERM
import org.ionproject.core.readApi.course.sql.CourseData.ID
import org.ionproject.core.readApi.course.sql.CourseData.NAME
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CourseMapper : RowMapper<Course> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Course {
        return Course(
            rs.getInt(ID),
            rs.getString(ACR),
            rs.getString(NAME),
            rs.getString(CAL_TERM))
    }
}
