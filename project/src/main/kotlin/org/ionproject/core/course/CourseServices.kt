package org.ionproject.core.course

import org.ionproject.core.common.model.Course
import org.ionproject.core.course.coursesDb.CourseRepo
import org.springframework.stereotype.Component

@Component
class CourseServices(private val repo : CourseRepo) {
    fun getCourses(): List<Course> {
        return repo.getCourses()
    }

    fun getCourseById(id: Int): Course? {
        return repo.getCourseById(id)
    }


}