package org.ionproject.core.userApi.klass.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.klass.model.UserKlassSection

private data class UserKlassSectionProps(
    val id: String,
    val courseId: Int,
    val courseAcr: String,
    val calendarTerm: String
)

private fun UserKlassSection.toProps() = UserKlassSectionProps(
    id,
    courseId,
    courseAcr,
    calendarTerm
)

// TODO: add link to class section actions
fun UserKlassSection.toSirenRepresentation(classId: Int) =
    SirenBuilder(toProps())
        .link("self", href = Uri.forUserClassSection(classId, id))
        .link(Uri.relClassSection, href = Uri.forClassSectionById(courseId, calendarTerm, id))
        .link(Uri.relUserClassSectionActions, href = Uri.forUserClassSectionActions(classId, id))
        .toSiren()
