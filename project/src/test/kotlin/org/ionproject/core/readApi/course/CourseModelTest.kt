package org.ionproject.core.readApi.course

import org.ionproject.core.readApi.course.model.Course
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CourseModelTest {

    @Test
    fun createCourseModel_shouldMatchEveryValueAppliedToCtor() {
        val id = 1
        val acr = "SL"
        val name = "Software Laboratory"
        val calterm = "1718v"
        val course = Course(id, acr, name, calterm)

        Assertions.assertEquals(id, course.id)
        Assertions.assertEquals(acr, course.acronym)
        Assertions.assertEquals(name, course.name)
        Assertions.assertEquals(calterm, course.term)
    }
}

