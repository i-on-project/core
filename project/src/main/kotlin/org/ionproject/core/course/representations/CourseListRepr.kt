package org.ionproject.core.course.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.modelInterfaces.ICourse
import org.springframework.http.HttpMethod
import java.net.URI

/*
 * TODO: TODOYEAR
 *  OBJETO URI EXPLODE COM "/v0/courses{?limit,page}"
 */


fun courseToListRepr(courses : List<ICourse>) = SirenBuilder()
        .klass("course", "collection")
        .entities(
                courses.map {
                    buildSubentities(it)
                }
        )
        .action(Action(name = "add-item", title = "Add a new Course", method = HttpMethod.POST,
                href = URI(COURSES_PATH), isTemplated = false, type = JSON_MEDIA_TYPE))
        .action(Action(name = "search", title = "Search Items", method = HttpMethod.GET,
                href = URI("${COURSES_PATH}limitpage"), isTemplated = true, type = SIREN_MEDIA_TYPE,
                fields = listOf(
                        Field(name = "limit", type = "number", klass = "param/limit"),
                        Field(name = "page", type = "number", klass = "param/page")
                )
        )).toSiren()

private fun buildSubentities(course : ICourse) : EmbeddedRepresentation = SirenBuilder(course)
        .klass("course")
        .rel("item")
        .link("self", URI("$COURSES_PATH/${course.acronym}"))
        .link("current", URI("$COURSES_PATH/${course.acronym}/$CLASS_ENTITY/TODOYEAR"))
        .link("collection", URI(COURSES_PATH))
        .toEmbed()


