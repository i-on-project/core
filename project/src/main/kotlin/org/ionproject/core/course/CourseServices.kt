package org.ionproject.core.course

import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.ionproject.core.common.model.Course
import org.springframework.stereotype.Component

@Component
class CourseServices(private val repo : CourseRepo) {
    fun getCourses(page : Int, limit : Int, flagDefaultValues : Boolean): List<Course> {
        if(page < 0 || limit < 0)
            throw IncorrectParametersException("The parameters limit:$limit or page:$page, can't have negative values.")

        return repo.getCourses(page, limit, flagDefaultValues)
    }

    fun getCourseById(id: Int): Course {
        return repo.getCourseById(id)
    }
}