package org.ionproject.core.course

import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.Media
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.course.model.Course
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

internal class CourseControllerTest : ControllerTester() {

    companion object {
        fun getCourse() =
            Course(1, "SL", "Software Laboratory", "1718v")

        fun getCurrentCourse(c: Course) =
            Course(c.id, c.acronym, c.name, "1920i")

        fun getCourseCollection() = listOf(
            Course(1, "SL", "Software Laboratory", "1920i"),
            Course(2, "WAD", "Web Applications Development", "1920v")
        )
    }

    /**
     * 200 OK
     */
    @Test
    fun getClassSection_shouldRespondWithSirenType() {
        isValidSiren(Uri.forClassSectionById(2, "1920v", "LI61D")).andReturn()
    }

    @Test
    fun getCourse_shouldRespondWithTheExactSirenRepresentationOfCourse() {
        val course = getCourse()
        val current = getCurrentCourse(course)
        val selfHref = Uri.forCourseById(course.id)

        data class OutputModel(val id: Int, val acronym: String, val name: String? = null)

        Assertions.assertNotNull(course.term)
        Assertions.assertNotNull(current.term)

        val expected = SirenBuilder(OutputModel(course.id, course.acronym, course.name))
            .klass("course")
            .entities(
                SirenBuilder()
                    .klass("class", "collection")
                    .rel(Uri.relClass)
                    .link("self", href = Uri.forKlasses(course.id))
                    .link("about", href = Uri.forCourseById(course.id))
                    .toEmbed()
            )
            .link("self", href = Uri.forCourseById(course.id))
            .link(
                "current",
                href = Uri.forKlassByCalTerm(
                    current.id,
                    current.term
                        ?: throw AssertionError("The Calendar Term of the current course must not be null")
                )
            )
            .link("collection", href = Uri.forCourses())
            .toSiren()

        isValidSiren(selfHref)
            .andDo { println() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getCourseCollection_shouldRespondWithTheExactSirenRepresentationOfCourseCollection() {
        val list = getCourseCollection()
        val page = 0
        val limit = 2
        val selfHref = Uri.forCourses()
        val selfHrefPaged = Uri.forPagingCourses(page, limit)

        data class OutputModel(val id: Int, val acronym: String)

        val expected = SirenBuilder()
            .klass("course", "collection")
            .entities(
                list.map { course ->
                    SirenBuilder(OutputModel(course.id, course.acronym))
                        .klass("class")
                        .rel("item")
                        .link("self", href = Uri.forCourseById(course.id))
                        .link(
                            "current",
                            href = Uri.forKlassByCalTerm(
                                course.id,
                                course.term
                                    ?: throw AssertionError("The Calendar Term of the current course must not be null")
                            )
                        )
                        .link("collection", href = selfHref)
                        .toEmbed()
                }
            ).action(
                Action(
                    name = "search",
                    title = "Search Items",
                    method = HttpMethod.GET,
                    href = UriTemplate("${selfHref}${Uri.rfcPagingQuery}"),
                    isTemplated = true,
                    type = Media.SIREN_TYPE,
                    fields = listOf(
                        Field(name = "limit", type = "number", klass = "param/limit"),
                        Field(name = "page", type = "number", klass = "param/page")
                    )
                )
            )
            .link("self", href = selfHrefPaged)
            .link("next", href = Uri.forPagingCourses(page + 1, limit))
            .toSiren()

        isValidSiren(selfHrefPaged, true)
            .andDo { println() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
