package org.ionproject.core.user.common.repr

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.user.common.model.User

data class ShortUserRepresentation(val userId: String, val name: String?, val email: String)

fun User.toSirenRepresentation() =
    SirenBuilder(this)
        .klass("user")
        .link("self", href = Uri.forUser(userId))
        .link("user", "class", href = Uri.forUserClasses(userId))
        .toSiren()

fun List<User>.toSirenRepresentation(pagination: Pagination) =
    SirenBuilder(pagination)
        .klass("user", "collection")
        .entities(map { it.toShortRepresentation() })
        .link("next", href = Uri.forPagingUsers(pagination.page + 1, pagination.limit))
        .apply {
            if (pagination.page > 0)
               link("prev", href = Uri.forPagingUsers(pagination.page - 1, pagination.limit))
        }
        .toSiren()

private fun User.toShortRepresentation() =
    SirenBuilder(ShortUserRepresentation(userId, name, email))
        .klass("user")
        .rel("item")
        .link("self", href = Uri.forUser(userId))
        .toEmbed()
