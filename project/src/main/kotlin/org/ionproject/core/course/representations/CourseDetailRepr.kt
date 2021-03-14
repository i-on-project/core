package org.ionproject.core.course.representations

import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.course.model.Course

/**
 * Output models
 */
private data class CourseReducedOutputModel(val id: Int, val acronym: String, val name: String? = null)

fun Course.courseToDetailRepr(): Siren {
    val builder = SirenBuilder(CourseReducedOutputModel(id, acronym, name))
        .klass("course")
        .entities(buildSubentities(id))
        .link("self", href = Uri.forCourseById(id))

    term?.let { builder.link("current", href = Uri.forKlassByCalTerm(id, it)) }

    return builder
        .link("collection", href = Uri.forCourses())
        .toSiren()
}

private fun buildSubentities(courseId: Int) =
    SirenBuilder()
        .klass("class", "collection")
        .rel(Uri.relClass)
        .link("self", href = Uri.forKlasses(courseId))
        .link("about", href = Uri.forCourseById(courseId))
        .toEmbed()
