package org.ionproject.core.course.representations

import org.ionproject.core.common.Action
import org.ionproject.core.common.EmbeddedRepresentation
import org.ionproject.core.common.Field
import org.ionproject.core.common.Media
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.course.model.Course
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

/**
 * Output models
 */
data class SmallCourseRepr(val id: Int, val acronym: String)

/**
 * Siren representation generators
 */
fun List<Course>.courseToListRepr(page: Int, limit: Int) =
    SirenBuilder()
        .klass("course", "collection")
        .entities(this.map { course -> course.buildSubentities() })
        .action(
            Action(
                name = "search",
                title = "Search Items",
                method = HttpMethod.GET,
                href = UriTemplate("${Uri.forCourses()}${Uri.rfcPagingQuery}"),
                isTemplated = true,
                type = Media.SIREN_TYPE,
                fields = listOf(
                    Field(name = "limit", type = "number", klass = "param/limit"),
                    Field(name = "page", type = "number", klass = "param/page")
                )
            )
        )
        .link("self", href = Uri.forPagingCourses(page, limit))
        .link("next", href = Uri.forPagingCourses(page + 1, limit)).let {
            {
                if (page > 0)
                    it.link("previous", href = Uri.forPagingCourses(page - 1, limit))
                it
            }()
        }
        .toSiren()

private fun Course.buildSubentities(): EmbeddedRepresentation {
    val builder = SirenBuilder(SmallCourseRepr(id, acronym))
        .klass("class")
        .rel("item")
        .link("self", href = Uri.forCourseById(id))

    term?.let { builder.link("current", href = Uri.forKlassByCalTerm(id, it)) }

    return builder.link("collection", href = Uri.forCourses())
        .toEmbed()
}
