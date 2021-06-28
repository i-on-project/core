package org.ionproject.core.userApi.klass.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.klass.model.UserKlassSection

private data class ShortUserKlassSectionProps(
    val id: String,
    val classId: Int,
    val courseId: Int,
    val courseAcr: String,
    val calendarTerm: String
)

private fun UserKlassSection.toShortProps() = ShortUserKlassSectionProps(
    id,
    classId,
    courseId,
    courseAcr,
    calendarTerm
)

private fun UserKlassSection.toEmbedRepresentation() =
    SirenBuilder(toShortProps())
        .klass("user", "class", "section")
        .rel("item")
        .link("self", href = Uri.forUserClassSection(classId, id))
        .link(Uri.relClassSection, href = Uri.forClassSectionById(courseId, calendarTerm, id))
        .toEmbed()

fun List<UserKlassSection>.toSirenRepresentation(pagination: Pagination) =
    SirenBuilder(pagination)
        .klass("user", "class", "section", "collection")
        .entities(map { it.toEmbedRepresentation() })
        .link("self", href = Uri.forPagingUserClassSections(pagination.page, pagination.limit))
        .link("next", href = Uri.forPagingUserClassSections(pagination.page + 1, pagination.limit))
        .apply {
            if (pagination.page > 0)
                link("previous", href = Uri.forPagingUserClassSections(pagination.page - 1, pagination.limit))
        }
        .toSiren()
