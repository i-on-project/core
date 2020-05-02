package org.ionproject.core.course.representations

import org.ionproject.core.common.*
import org.ionproject.core.course.model.Course
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

/**
 * Output models
 */
data class SmallCourseRepr(val acronym: String)

/**
 * Siren representation generators
 */
fun List<Course>.courseToListRepr(page: Int, limit: Int) = SirenBuilder()
    .klass("course", "collection")
    .entities(this.map { course -> course.buildSubentities() })
    .action(
        Action(
            name = "add-item",
            title = "Add a new Course",
            method = HttpMethod.POST,
            href = Uri.forCourses().toTemplate(),
            isTemplated = false,
            type = Media.APPLICATION_JSON
        )
    )
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
    .link("self", Uri.forPagingCourses(page, limit))
    .link("next", Uri.forPagingCourses(page + 1, limit)).let {
        {
            if (page > 0)
                it.link("previous", Uri.forPagingCourses(page - 1, limit))
            it
        }()
    }
    .toSiren()

private fun Course.buildSubentities(): EmbeddedRepresentation =
    SirenBuilder(SmallCourseRepr(acronym))
        .klass("course")
        .rel("item")
        .link("self", Uri.forCourseById(id))
        .link("current", Uri.forKlassByCalTerm(id, term!!))
        .link("collection", Uri.forCourses())
        .toEmbed()

