package org.ionproject.core.course.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.ICourse
import org.springframework.http.HttpMethod
import java.net.URI

fun courseToDetailRepr(course : ICourse) = SirenBuilder(course)
        .klass("course")
        .entities(listOf(buildSubentities(course.id, course.acronym, course.name)))
        .action(Action(name = "delete", title = "delete course", method = HttpMethod.DELETE,
                href = URI("${COURSES_PATH}/${course.id}"), isTemplated = true))
        .action(Action(name = "edit", title = "edit course", method = HttpMethod.PATCH,
                href = URI("${COURSES_PATH}/${course.id}"), isTemplated = false))
        .toSiren()


private fun buildSubentities(courseId: Int,
                             acronym : String,
                             name : String) : EmbeddedRepresentation = SirenBuilder()
        .klass("class", "collection")
        .rel(REL_CLASS)
        .link("self", URI("${COURSES_PATH}/${courseId}/${CLASS_ENTITY}"))
        .link("course", URI("${COURSES_PATH}/${courseId}/"))
        .toEmbed()
