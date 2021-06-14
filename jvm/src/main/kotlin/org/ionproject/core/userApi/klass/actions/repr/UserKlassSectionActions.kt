package org.ionproject.core.userApi.klass.actions.repr

import org.ionproject.core.common.Action
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

object UserKlassSectionActions {

    fun toSirenRepresentation(classId: Int, sectionId: String, isSubscribed: Boolean) =
        SirenBuilder()
            .klass("user", "class", "section", "action")
            .apply {
                if (isSubscribed) {
                    action(
                        Action(
                            name = "unsubscribe",
                            href = UriTemplate(Uri.forUserClassSection(classId, sectionId).toString()),
                            title = "Unsubscribe From Class",
                            method = HttpMethod.DELETE
                        )
                    )
                } else {
                    action(
                        Action(
                            name = "subscribe",
                            href = UriTemplate(Uri.forUserClassSection(classId, sectionId).toString()),
                            title = "Subscribe to Class Section",
                            method = HttpMethod.PUT
                        )
                    )
                }
            }
            .link("self", href = Uri.forUserClassSectionActions(classId, sectionId))
            .toSiren()
}
