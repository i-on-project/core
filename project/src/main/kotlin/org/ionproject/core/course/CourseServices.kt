package org.ionproject.core.course

import org.ionproject.core.common.APP_MODE
import org.ionproject.core.common.MockMode
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.course.CoursesDal.ICourseRepo
import org.springframework.stereotype.Component

@Component
class CourseServices(@MockMode(APP_MODE) private val repo : ICourseRepo) {
    fun getCourses(): List<ICourse> {
        return repo.readCourses()
    }

    fun getCourse(acr: String): ICourse {
        return repo.readCourse(acr)
    }


}