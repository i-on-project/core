package org.ionproject.core.course.CoursesDal

import org.ionproject.core.common.MockMode
import org.ionproject.core.common.model.Course
import org.springframework.stereotype.Component

@Component
@MockMode(false)
class CourseRepo : ICourseRepo {
    override fun readCourses(): List<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readCourse(acr: String): Course {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}