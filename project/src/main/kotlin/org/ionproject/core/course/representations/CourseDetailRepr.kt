package org.ionproject.core.course.representations

import org.ionproject.core.common.Action
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.course.model.Course
import org.ionproject.core.common.toTemplate
import org.springframework.http.HttpMethod

/**
 * Output models
 */
private data class CourseReducedOutputModel(val id: Int, val acronym: String, val name: String? = null)

fun Course.courseToDetailRepr() =
    SirenBuilder(CourseReducedOutputModel(id,acronym, name))
        .klass("course")
        .entities(buildSubentities(id))
        .action(
            Action(
                    name = "delete",
                    title = "delete course",
                    method = HttpMethod.DELETE,
                    href = Uri.forCourseById(id).toTemplate(),
                    isTemplated = false,
                    fields = listOf()
            )
        )
        .action(
            Action(
                    name = "edit",
                    title = "edit course",
                    method = HttpMethod.PATCH,
                    href = Uri.forCourseById(id).toTemplate(),
                    isTemplated = false,
                    fields = listOf()
            )
        )
        .link("self", href = Uri.forCourseById(id))
        .link("current", href = Uri.forKlassByCalTerm(id, term!!))
        .link("collection", href = Uri.forCourses())
        .toSiren()


private fun buildSubentities(courseId: Int) =
    SirenBuilder()
        .klass("class", "collection")
        .rel(Uri.relClass)
        .link("self", href = Uri.forKlasses(courseId))
        .link("about", href = Uri.forCourseById(courseId))
        .toEmbed()
