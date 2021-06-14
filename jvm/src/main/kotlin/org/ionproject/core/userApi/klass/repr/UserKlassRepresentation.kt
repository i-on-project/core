package org.ionproject.core.userApi.klass.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.klass.model.UserKlass

private data class UserKlassProps(
    val id: Int,
    val courseId: Int,
    val courseAcr: String,
    val calendarTerm: String
)

private fun UserKlass.toProps() = UserKlassProps(
    id,
    courseId,
    courseAcr,
    calendarTerm
)

// TODO: add link to user actions
fun UserKlass.toSirenRepresentation() =
    SirenBuilder(toProps())
        .klass("user", "class")
        .entities(getEmbedEntities(id))
        .link("self", href = Uri.forUserClass(id))
        .link(Uri.relClass, href = Uri.forKlassByCalTerm(courseId, calendarTerm))
        .link(Uri.relUserClassActions, href = Uri.forUserClassActions(id))
        .toSiren()

private fun UserKlass.getEmbedEntities(classId: Int) =
    sections?.map {
        SirenBuilder(mapOf("sectionId" to it))
            .klass("user", "class", "section")
            .rel("item")
            .link("self", href = Uri.forUserClassSection(classId, it))
            .link(Uri.relClassSection, href = Uri.forClassSectionById(courseId, calendarTerm, it))
            .toEmbed()
    }
