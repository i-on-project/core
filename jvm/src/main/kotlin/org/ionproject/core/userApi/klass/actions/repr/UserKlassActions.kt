package org.ionproject.core.userApi.klass.actions.repr

import org.ionproject.core.common.Action
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

object UserKlassActions {

    fun toSirenRepresentation(classId: Int, isSubscribed: Boolean) =
        SirenBuilder()
            .klass("user", "class", "action")
            .apply {
                if (isSubscribed) {
                    action(
                        Action(
                            name = "unsubscribe",
                            href = UriTemplate(Uri.forUserClass(classId).toString()),
                            title = "Unsubscribe From Class",
                            method = HttpMethod.DELETE
                        )
                    )
                } else {
                    action(
                        Action(
                            name = "subscribe",
                            href = UriTemplate(Uri.forUserClass(classId).toString()),
                            title = "Subscribe to Class",
                            method = HttpMethod.PUT
                        )
                    )
                }
            }
            .link("self", href = Uri.forUserClassActions(classId))
            .toSiren()
}
