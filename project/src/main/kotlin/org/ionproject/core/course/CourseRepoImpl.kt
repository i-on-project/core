package org.ionproject.core.course

import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.course.model.Course
import org.springframework.stereotype.Component

@Component
class CourseRepoImpl(
    private val tm: TransactionManager,
    private val courseMapper: CourseMapper
) : CourseRepo {

    override fun getCourses(page: Int, limit: Int): List<Course> {
        val result = tm.run { handle ->
            handle.createQuery("SELECT * FROM courseWithTerm order by id OFFSET :offset LIMIT :lim")
                    .bind("offset", page * limit)
                    .bind("lim", limit)
                    .map(courseMapper)
                    .list()
        } as List<Course>

        if(result.isEmpty()) {
            if(page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        return result
    }

    override fun getCourseById(id: Int): Course? = tm.run { handle ->
        handle.createQuery("SELECT * FROM courseWithTerm WHERE id=:id order by id")
                .bind("id", id)
                .map(courseMapper)
                .firstOrNull()
    }

}