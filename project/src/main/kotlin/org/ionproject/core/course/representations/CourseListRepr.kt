package org.ionproject.core.course.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.Course
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

fun courseToListRepr(courses: List<Course>, page: Int, limit: Int) = SirenBuilder()
    .klass("course", "collection")
    .entities(
        courses.map {
            buildSubentities(it)
        }
    )
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
        ))
    .link("self", Uri.forPagingCourses(page, limit))
    .link("next", Uri.forPagingCourses(page + 1, limit)).let {
        {
            if (page > 0)
                it.link("previous", Uri.forPagingCourses(page - 1, limit))
            it
        }()
    }
    .toSiren()

private fun buildSubentities(course: Course): EmbeddedRepresentation =
    SirenBuilder(SmallCourseRepr(course.acronym))
        .klass("course")
        .rel("item")
        .link("self", Uri.forCourseById(course.id))
        .link("current", Uri.forKlassByCalTerm(course.id, course.term!!))
        .link("collection", Uri.forCourses())
        .toEmbed()

data class SmallCourseRepr(val acronym: String)
