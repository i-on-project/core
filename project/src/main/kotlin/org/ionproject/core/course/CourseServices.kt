package org.ionproject.core.course

import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.ionproject.core.common.model.Course
import org.springframework.stereotype.Component

@Component
class CourseServices(private val repo : CourseRepo) {
    fun getCourses(page : Int, limit : Int): List<Course>? {
        if(page < 0 || limit < 0)
            throw IncorrectParametersException("?page=$page&limit=$limit")

        return repo.getCourses(page, limit)
    }

    fun getCourseById(id: Int): Course? {
        return repo.getCourseById(id)
    }
}