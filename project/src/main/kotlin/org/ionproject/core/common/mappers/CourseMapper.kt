package org.ionproject.core.common.mappers

import org.ionproject.core.common.model.Course
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class CourseMapper : RowMapper<Course> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Course {
        return Course(rs.getInt("id"), rs.getString("acronym"), rs.getString("name"), rs.getString("term"))
    }

}