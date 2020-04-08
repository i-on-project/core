package org.ionproject.core.course.coursesDb

import org.ionproject.core.common.mappers.CourseMapper
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Component

@Component
class CourseRepoImpl(private val tm : TransactionManager) : CourseRepo {
    val courseMapper : CourseMapper = CourseMapper()

    override fun getCourses(): List<Course> {
        val result = tm.run {
            handle -> handle.createQuery("SELECT * FROM courseWithTerm")
                .map(courseMapper)
                .list()
        }
        return result ?: listOf()
    }

    override fun getCourseById(id: Int): Course? {
        val result = tm.run {
            handle -> handle.createQuery("SELECT * FROM courseWithTerm WHERE id=:id")
                .bind("id", id)
                .map(courseMapper)
                .one()
        }

        return result
    }
}