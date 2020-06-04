package org.ionproject.core.course

import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.course.model.Course
import org.ionproject.core.course.sql.CourseData.GET_COURSES_QUERY
import org.ionproject.core.course.sql.CourseData.GET_COURSE_QUERY
import org.ionproject.core.course.sql.CourseData.ID
import org.ionproject.core.course.sql.CourseData.LIMIT
import org.ionproject.core.course.sql.CourseData.OFFSET
import org.ionproject.core.course.sql.CourseMapper
import org.springframework.stereotype.Component

@Component
class CourseRepoImpl(
        private val tm: TransactionManager,
        private val courseMapper: CourseMapper
) : CourseRepo {

    override fun getCourses(page: Int, limit: Int): List<Course> {
        val result = tm.run { handle ->
            handle.createQuery(GET_COURSES_QUERY)
                .bind(OFFSET, page * limit)
                .bind(LIMIT, limit)
                .map(courseMapper)
                .list()
        } as List<Course>

        if (result.isEmpty()) {
            if (page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        return result
    }

    override fun getCourseById(id: Int): Course? = tm.run { handle ->
        handle.createQuery(GET_COURSE_QUERY)
            .bind(ID, id)
            .map(courseMapper)
            .firstOrNull()
    }

}