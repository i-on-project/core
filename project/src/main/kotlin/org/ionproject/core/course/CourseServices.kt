package org.ionproject.core.course

import org.ionproject.core.common.model.Course
import org.springframework.stereotype.Component

@Component
class CourseServices(private val repo : CourseRepoImpl) {
    fun getCourses(page : Int, limit : Int): List<Course> {
        return repo.getCourses(page, limit)
    }

    fun getCourseById(id: Int): Course {
        return repo.getCourseById(id)
    }
}