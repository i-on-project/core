package org.ionproject.core.userApi.user.repr

import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.user.model.User
import org.springframework.http.HttpMethod
import java.time.Instant

private data class UserSirenProps(
    val userId: String,
    val name: String? = null,
    val email: String,
    val createdAt: Instant = Instant.now()
)

private fun User.toSirenProps() = UserSirenProps(
    userId,
    name,
    email,
    createdAt
)

fun User.toSirenRepresentation() =
    SirenBuilder(toSirenProps())
        .klass("user")
        .link("self", href = Uri.forUser(userId))
        .link("user", "class", href = Uri.forUserClasses(userId))
        .action(
            Action(
                "edit-user",
                href = Uri.userTemplate, // TODO: improve actions href
                title = "Edit User",
                method = HttpMethod.PUT,
                type = "application/json",
                isTemplated = true,
                fields = listOf(
                    Field("name", type = "text")
                )
            )
        )
        .action(
            Action(
                "delete-user",
                href = Uri.userTemplate, // TODO: improve actions href
                title = "Delete User",
                method = HttpMethod.DELETE,
                type = "application/json",
                isTemplated = true
            )
        )
        .toSiren()
