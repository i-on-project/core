package org.ionproject.core.course

import org.ionproject.core.common.Action
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.toTemplate
import org.ionproject.core.course.model.Course
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class CourseControllerTest : ControllerTester() {

    companion object {
        fun getCourse() =
            Course(1, "SL", "Software Laboratory", "1718v")

        fun getCurrentCourse() =
            Course(1, "SL", "Software Laboratory", "2021i")
    }

    /**
     * 200 OK
     */
    @Test
    fun getClassSection_shouldRespondWithSirenType() {
        isValidSiren(Uri.forClassSectionById(1, "1920v", "1D")).andReturn()
    }

    private data class CourseReducedOutputModel(val id: Int, val acronym: String, val name: String? = null)
    @Test
    fun getClass_shouldRespondWithTheExactSirenRepresentationOfClass() {
        val course = getCourse()
        val current = getCurrentCourse()
        val selfHref = Uri.forCourseById(course.id)

        Assertions.assertNotNull(course.term)
        Assertions.assertNotNull(current.term)

        current.term?.let {
            val expected =
                SirenBuilder(CourseReducedOutputModel(course.id, course.acronym, course.name))
                    .klass("course")
                    .entities(listOf(
                        SirenBuilder()
                            .klass("class", "collection")
                            .rel(Uri.relClass)
                            .link("self", href = Uri.forKlasses(course.id))
                            .link("about", href = Uri.forCourseById(course.id))
                            .toEmbed()))
                    .action(
                        Action(
                            name = "delete",
                            title = "delete course",
                            method = HttpMethod.DELETE,
                            href = Uri.forCourseById(course.id).toTemplate(),
                            isTemplated = false,
                            fields = listOf()
                        )
                    )
                    .action(
                        Action(
                            name = "edit",
                            title = "edit course",
                            method = HttpMethod.PATCH,
                            href = Uri.forCourseById(course.id).toTemplate(),
                            isTemplated = false,
                            fields = listOf()
                        )
                    )
                    .link("self", href = Uri.forCourseById(course.id))
                    .link("current", href = Uri.forKlassByCalTerm(current.id, it))
                    .link("collection", href = Uri.forCourses())
                    .toSiren()

            isValidSiren(selfHref)
                .andDo { print() }
                .andExpect { expected.matchMvc(this) }
                .andReturn()
        }
    }
}
