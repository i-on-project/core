package org.ionproject.core.userApi.klass.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.klass.model.UserKlassSection

private data class UserKlassSectionProps(
    val id: String,
    val classId: Int,
    val courseId: Int,
    val courseAcr: String,
    val courseName: String,
    val calendarTerm: String
)

private fun UserKlassSection.toProps() = UserKlassSectionProps(
    id,
    classId,
    courseId,
    courseAcr,
    courseName,
    calendarTerm
)

fun UserKlassSection.toSirenRepresentation(classId: Int) =
    SirenBuilder(toProps())
        .klass("user", "class", "section")
        .link("self", href = Uri.forUserClassSection(classId, id))
        .link("collection", href = Uri.forPagingUserClassSections(0, 0))
        .link(Uri.relClassSection, href = Uri.forClassSectionById(courseId, calendarTerm, id))
        .link(Uri.relUserClassSectionActions, href = Uri.forUserClassSectionActions(classId, id))
        .toSiren()
