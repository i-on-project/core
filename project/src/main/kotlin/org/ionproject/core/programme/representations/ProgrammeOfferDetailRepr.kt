package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.ProgrammeOffer
import org.springframework.http.HttpMethod


fun offerToDetailRepr(offer: ProgrammeOffer) =
        SirenBuilder(ShortOfferForOfferRepr(offer.id, offer.courseAcr, offer.termNumber, offer.optional))
        .klass("offer")
        .entities(
                listOf(buildSubentities(offer.courseId))
        )
        .action(
                Action(
                        name = "edit",
                        title = "edit offer",
                        method = HttpMethod.PUT,
                        type = Media.APPLICATION_JSON,
                        href = Uri.forProgrammeOfferById(offer.programmeId, offer.id),
                        fields = listOf(
                                Field(name = "Acronym", type = "text"),
                                Field(name = "TermNumber", type = "number"),
                                Field(name = "Credits", type = "number"),
                                Field(name = "Optional", type = "boolean"),
                                Field(name = "Precedents", type = "list")
                        )
                )
        )
        .link("self", Uri.forProgrammeOfferById(offer.programmeId, offer.id))
        .link("related", Uri.forProgrammesById(offer.id))
        .toSiren()

private fun buildSubentities(courseId : Int) = SirenBuilder()
        .klass("course")
        .rel(Uri.REL_COURSE)
        .link("self", Uri.forCourseById(courseId))
        .toEmbed()

data class ShortOfferForOfferRepr(val id : Int, val acronym: String, val termNumber: Int, val optional: Boolean)