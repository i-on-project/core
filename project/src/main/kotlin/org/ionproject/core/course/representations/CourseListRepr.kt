package org.ionproject.core.course.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.Course
import org.springframework.http.HttpMethod

/*
 * TODO: TODOYEAR
 *  OBJETO URI EXPLODE COM "/v0/courses{?limit,page}"
 */


fun courseToListRepr(courses : List<Course>) = SirenBuilder()
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
                        href = Uri.forCourses(),
                        isTemplated = false,
                        type = JSON_MEDIA_TYPE
                )
        )
        .action(
                Action(
                        name = "search",
                        title = "Search Items",
                        method = HttpMethod.GET,
                        href = Uri.forCourses(),        //TODO TEMPLATING
                        isTemplated = true,
                        type = SIREN_MEDIA_TYPE,
                fields = listOf(
                        Field(name = "limit", type = "number", klass = "param/limit"),
                        Field(name = "page", type = "number", klass = "param/page")
                )
        )).toSiren()

private fun buildSubentities(course : Course) : EmbeddedRepresentation =
        SirenBuilder(smallCourseRepr(course.acronym))
            .klass("course")
            .rel("item")
            .link("self", Uri.forCourseById(course.id))
            .link("current", Uri.forKlassByTerm(course.acronym, course.term))
            .link("collection", Uri.forCourses())
            .toEmbed()

data class smallCourseRepr(val acronym : String)
