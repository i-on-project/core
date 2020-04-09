package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.springframework.http.HttpMethod


/*
 * Builds the output model representations
 */
fun programmeToDetailRepr(programme : Programme) =
        SirenBuilder(shortProgrammeReprWithoutOffer(programme.id, programme.name, programme.acronym, programme.termSize))
        .entities(
                programme.offers.map {
                    buildSubentities(programme, it)
                }
        )
        .action(
                Action(
                        name = "edit-programme",
                        title = "Edit Programme",
                        method = HttpMethod.PUT,
                        href = Uri.forProgrammesById(programme.id),
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
                        href = Uri.forProgrammesByIdOffer(programme.id),
                        type = Media.APPLICATION_JSON,
                        fields = listOf(
                                Field(name = "CourseId", type = "number"),
                                Field(name = "CurricularTerm", type = "number"),
                                Field(name = "Optional", type = "boolean")
                        )
                )
        )
        .link("self", href = Uri.forProgrammesById(programme.id))
        .link("up", href = Uri.forProgrammes())
        .toSiren()

private fun buildSubentities(programme: Programme, offer: ProgrammeOffer) : EmbeddedRepresentation =
        SirenBuilder(shortOfferRepr(offer.courseId, offer.termNumber))
            .klass("offer")
            .title("${offer.courseAcr} Offer")
            .rel(Uri.REL_PROGRAMME_OFFER)
            .link("self", Uri.forProgrammeOfferById(programme.id, offer.id))
            .toEmbed()


//This or JsonIgnore?
data class shortProgrammeReprWithoutOffer(val id: Int, val name: String, val acronym: String, val termSize: Int)

//Is this the best way?
data class shortOfferRepr(val courseId : Int, val termNumber: Int)