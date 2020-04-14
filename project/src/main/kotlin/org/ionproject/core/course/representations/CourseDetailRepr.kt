package org.ionproject.core.course.representations

import org.ionproject.core.common.Action
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.model.Course
import org.springframework.http.HttpMethod

fun courseToDetailRepr(course: Course) =
    SirenBuilder(CourseSmallDetails(course.acronym, course.name!!))
        .klass("course")
        .entities(listOf(buildSubentities(course.id)))
        .action(
            Action(
                name = "delete",
                title = "delete course",
                method = HttpMethod.DELETE,
                href = Uri.forCourseById(course.id),
                isTemplated = true))
        .action(
            Action(
                name = "edit",
                title = "edit course",
                method = HttpMethod.PATCH,
                href = Uri.forCourseById(course.id),
                isTemplated = false))
        .link("self", Uri.forCourseById(course.id))
        .link("current", Uri.forKlassByTerm(course.id, course.term!!))
        .link("collection", Uri.forCourses())
        .toSiren()


private fun buildSubentities(courseId: Int) =
    SirenBuilder()
        .klass("class", "collection")
        .rel(Uri.REL_CLASS)
        .link("self", Uri.forKlasses(courseId))
        .link("course", Uri.forCourseById(courseId))
        .toEmbed()

private data class CourseSmallDetails(val acronym: String, val name: String)