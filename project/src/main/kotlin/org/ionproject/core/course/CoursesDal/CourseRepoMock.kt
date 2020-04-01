package org.ionproject.core.course.CoursesDal

import org.ionproject.core.common.model.Course
import org.ionproject.core.course.CoursesDal.ICourseRepo
import org.springframework.stereotype.Component

@Component("repoMock")
class CourseRepoMock : ICourseRepo {
    override fun readCourses(): List<Course> {
        return listOf(
                Course.of(
                        acronym = "LS",
                        name = "Lab Software",
                        calendarId = 1
                )!!,
                Course.of(
                        acronym = "AED",
                        name = "Algs Estr Dados",
                        calendarId = 2
                )!!
        )
    }

    override fun readCourse(acr: String): Course {
        return Course.of(
                acronym = "LS",
                name = "Lab Software",
                calendarId = 1
        )!!
    }
}