package org.ionproject.core.course

import org.ionproject.core.common.model.Course
import org.ionproject.core.course.CoursesDal.ICourseRepo
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class CourseController(@Qualifier("repoMock") private val repo : ICourseRepo) {
    fun getCourses(): List<Course> {
        return repo.readCourses()
    }

    fun getCourse(acr: String): Course {
        return repo.readCourse(acr)
    }


}