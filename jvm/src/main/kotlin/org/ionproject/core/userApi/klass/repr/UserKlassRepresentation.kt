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
fun UserKlass.toSirenRepresentation(userId: String) =
    SirenBuilder(toProps())
        .klass("user", "class")
        .entities(getEmbedEntities(userId, id))
        .link("self", href = Uri.forUserClass(userId, id))
        .link(Uri.relClass, href = Uri.forKlassByCalTerm(courseId, calendarTerm))
        .toSiren()

private fun UserKlass.getEmbedEntities(userId: String, classId: Int) =
    sections?.map {
        SirenBuilder(mapOf("sectionId" to it))
            .klass("user", "class", "section")
            .rel("item")
            .link("self", href = Uri.forUserClassSection(userId, classId, it))
            .link(Uri.relClassSection, href = Uri.forClassSectionById(courseId, calendarTerm, it))
            .toEmbed()
    }
