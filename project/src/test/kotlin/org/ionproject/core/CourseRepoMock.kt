package org.ionproject.core

import org.ionproject.core.common.model.Course
import org.springframework.stereotype.Component

@Component
class CourseRepoMock : CourseRepo {
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