package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.http.HttpMethod
import java.net.URI


fun offerToDetailRepr(offer: IProgrammeOffer) = SirenBuilder(offer)
        .klass("offer")
        .entities(
                listOf(buildSubentities(offer.courseId))
        )
        .action(
                Action(
                        name = "edit", title = "edit offer", method = HttpMethod.PUT, type = JSON_MEDIA_TYPE,
                        href = URI("${Uri.programmes}/${offer.programmeId}/offers/${offer.id}"),
                        fields = listOf(
                                Field(name = "Acronym", type = "text"),
                                Field(name = "TermNumber", type = "number"),
                                Field(name = "Credits", type = "number"),
                                Field(name = "Optional", type = "boolean"),
                                Field(name = "Precedents", type = "list")
                        )
                )
        )
        .link("self", URI("${Uri.programmes}/${offer.programmeId}/offers/${offer.id}"))
        .link("related", URI("${Uri.programmes}/${offer.programmeId}"))
        .toSiren()

private fun buildSubentities(courseId : Int) = SirenBuilder()
        .klass("course")
        .rel(REL_COURSE)
        .link("self", URI("${Uri.courses}/${courseId}"))
        .toEmbed()

