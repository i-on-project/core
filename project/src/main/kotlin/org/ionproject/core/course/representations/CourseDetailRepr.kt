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
private data class CourseReducedOutputModel(val acronym: String, val name: String? = null)

fun Course.courseToDetailRepr() =
    SirenBuilder(CourseReducedOutputModel(acronym, name))
        .klass("course")
        .entities(listOf(buildSubentities(id)))
        .action(
            Action(
                name = "delete",
                title = "delete course",
                method = HttpMethod.DELETE,
                href = Uri.forCourseById(id).toTemplate(),
                isTemplated = true))
        .action(
            Action(
                name = "edit",
                title = "edit course",
                method = HttpMethod.PATCH,
                href = Uri.forCourseById(id).toTemplate(),
                isTemplated = false))
        .link("self", Uri.forCourseById(id))
        .link("current", Uri.forKlassByCalTerm(id, term!!))
        .link("collection", Uri.forCourses())
        .toSiren()


private fun buildSubentities(courseId: Int) =
    SirenBuilder()
        .klass("class", "collection")
        .rel(Uri.relClass)
        .link("self", Uri.forKlasses(courseId))
        .link("course", Uri.forCourseById(courseId))
        .toEmbed()
