package org.ionproject.core.userApi.klass.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.klass.model.UserKlass

private data class ShortUserKlassProps(
    val id: Int,
    val courseId: Int,
    val courseAcr: String,
    val calendarTerm: String
)

private fun UserKlass.toShortProps() = ShortUserKlassProps(
    id,
    courseId,
    courseAcr,
    calendarTerm
)

private fun UserKlass.toEmbedRepresentation(userId: String) =
    SirenBuilder(toShortProps())
        .klass("user", "class")
        .rel("item")
        .link("self", href = Uri.forUserClass(userId, id))
        .link(Uri.relClass, href = Uri.forKlassByCalTerm(courseId, calendarTerm))
        .toEmbed()

// TODO: add user actions link
fun List<UserKlass>.toSirenRepresentation(userId: String, pagination: Pagination) =
    SirenBuilder(pagination)
        .klass("user", "class", "collection")
        .entities(map { it.toEmbedRepresentation(userId) })
        .link("self", href = Uri.forPagingUserClasses(userId, pagination.page, pagination.limit))
        .link("next", href = Uri.forPagingUserClasses(userId, pagination.page + 1, pagination.limit))
        .apply {
            if (pagination.page > 0)
                link("previous", href = Uri.forPagingUserClasses(userId, pagination.page - 1, pagination.limit))
        }
        .toSiren()
