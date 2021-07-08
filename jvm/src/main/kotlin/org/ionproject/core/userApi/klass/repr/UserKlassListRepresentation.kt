package org.ionproject.core.userApi.klass.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.klass.model.UserKlass

private data class ShortUserKlassProps(
    val id: Int,
    val courseId: Int,
    val courseAcr: String,
    val courseName: String,
    val calendarTerm: String
)

private fun UserKlass.toShortProps() = ShortUserKlassProps(
    id,
    courseId,
    courseAcr,
    courseName,
    calendarTerm
)

private fun UserKlass.toEmbedRepresentation() =
    SirenBuilder(toShortProps())
        .klass("user", "class")
        .rel("item")
        .link("self", href = Uri.forUserClass(id))
        .link(Uri.relClass, href = Uri.forKlassByCalTerm(courseId, calendarTerm))
        .toEmbed()

fun Iterable<UserKlass>.toSirenRepresentation(pagination: Pagination) =
    SirenBuilder(pagination)
        .klass("user", "class", "collection")
        .entities(map { it.toEmbedRepresentation() })
        .link("self", href = Uri.forPagingUserClasses(pagination.page, pagination.limit))
        .link("next", href = Uri.forPagingUserClasses(pagination.page + 1, pagination.limit))
        .apply {
            if (pagination.page > 0)
                link("previous", href = Uri.forPagingUserClasses(pagination.page - 1, pagination.limit))
        }
        .toSiren()
