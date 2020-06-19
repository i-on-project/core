package org.ionproject.core.readApi.course

import org.ionproject.core.readApi.course.model.Course
import org.springframework.stereotype.Component

@Component
class CourseServices(private val repo: CourseRepoImpl) {
    fun getCourses(page: Int, limit: Int): List<Course> {
        return repo.getCourses(page, limit)
    }

    fun getCourseById(id: Int): Course? {
        return repo.getCourseById(id)
    }
}