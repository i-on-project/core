package org.ionproject.core.course

import org.ionproject.core.course.model.Course
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CourseMapper : RowMapper<Course> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Course {
        return Course(rs.getInt("id"), rs.getString("acronym"), rs.getString("name"), rs.getString("term"))
    }
}