package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class ShortProgrammeReprWithoutOffer(val id: Int, val name: String, val acronym: String, val termSize: Int)

data class ShortOfferRepr(val courseId : Int, val termNumber: Int)

/**
 * Builds the Siren representations
 */
fun Programme.programmeToDetailRepr() =
        SirenBuilder(ShortProgrammeReprWithoutOffer(id, name!!, acronym, termSize))
        .entities(
                offers.map { offer -> this.buildSubentities(offer) }
        )
        .action(
                Action(
                        name = "edit-programme",
                        title = "Edit Programme",
                        method = HttpMethod.PUT,
                        href = Uri.forProgrammesById(id).toTemplate(),
                        type = Media.APPLICATION_JSON,
                        fields = listOf(
                                Field(name = "ProgrammeName", type = "text"),   //name may be null, but the action allows to modify
                                Field(name = "Acronym", type = "text"),
                                Field(name = "TermSize", type = "number")
                        )
                )
        )
        .action(
                Action(
                        name = "add-offer",
                        title = "Add Offer",
                        method = HttpMethod.POST,
                        href = Uri.forProgrammesByIdOffer(id).toTemplate(),
                        type = Media.APPLICATION_JSON,
                        fields = listOf(
                                Field(name = "CourseId", type = "number"),
                                Field(name = "CurricularTerm", type = "number"),
                                Field(name = "Optional", type = "boolean")
                        )
                )
        )
        .link("self", href = Uri.forProgrammesById(id))
        .link("up", href = Uri.forProgrammes())
        .toSiren()

private fun Programme.buildSubentities(offer: ProgrammeOffer) : EmbeddedRepresentation =
        SirenBuilder(ShortOfferRepr(offer.courseId, offer.termNumber))
            .klass("offer")
            .title("${offer.courseAcr} Offer")
            .rel(Uri.relProgrammeOffer)
            .link("self", Uri.forProgrammeOfferById(this.id, offer.id))
            .toEmbed()
