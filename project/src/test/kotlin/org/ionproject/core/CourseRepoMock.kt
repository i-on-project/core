package org.ionproject.core

import org.ionproject.core.common.model.Course
import org.ionproject.core.course.coursesDb.ICourseRepo
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
class CourseRepoMock : ICourseRepo {
    override fun getCourses(): List<Course> {
        return listOf(
                Course.of(
                        id = 1,
                        acronym = "LS",
                        name = "Lab Software"
                )!!,
                Course.of(
                        id = 2,
                        acronym = "AED",
                        name = "Algs Estr Dados"
                )!!
        )
    }

    override fun getCourseByAcr(acr: String): Course {
        return Course.of(
                id = 1,
                acronym = "LS",
                name = "Lab Software"
        )!!
    }
}