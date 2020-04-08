package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.http.HttpMethod
import java.net.URI


/*
 * Builds the output model representations
 */
fun programmeToDetailRepr(programme : IProgramme) = SirenBuilder(programme)
        .entities(
                programme.offers.map {
                    buildSubentities(programme, it)
                }
        )
        .action(
                Action(name = "edit-programme", title = "Edit Programme",
                        method = HttpMethod.PUT,
                        href = URI("$PROGRAMMES_PATH/${programme.id}"),
                        type = JSON_MEDIA_TYPE,
                        fields = listOf(
                                Field(name = "ProgrammeName", type = "text"),   //name may be null, but the action allows to modify
                                Field(name = "Acronym", type = "text"),
                                Field(name = "TermSize", type = "number")
                        )
                )
        )
        .action(
                Action(name = "add-offer", title = "Add Offer", method = HttpMethod.POST,
                        href = URI("$PROGRAMMES_PATH/${programme.id}/offers"),
                        type = JSON_MEDIA_TYPE,
                        fields = listOf(
                                Field(name = "CourseId", type = "number"),
                                Field(name = "CurricularTerm", type = "number"),
                                Field(name = "Optional", type = "boolean")
                        )
                )
        )
        .link("self", href = URI("$PROGRAMMES_PATH/${programme.id}"))
        .link("up", href = URI("$PROGRAMMES_PATH/"))
        .toSiren()

private fun buildSubentities(programme: IProgramme, offer: IProgrammeOffer) : EmbeddedRepresentation =
        SirenBuilder(shortOfferRepr(offer.courseId, offer.termNumber))
            .klass("offer")
            .title("${offer.courseAcr} Offer")
            .rel(REL_PROGRAMME_OFFER)
            .link("self", URI("$PROGRAMMES_PATH/${programme.id}/offers/${offer.id}"))
            .toEmbed()


//Is this the best way?
data class shortOfferRepr(val courseId : Int, val termNumber: Int)