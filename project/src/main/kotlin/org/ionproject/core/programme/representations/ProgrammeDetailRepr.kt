package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class ShortProgrammeReprWithoutOffer(val id: Int, val name: String? = null, val acronym: String, val termSize: Int)

data class ShortOfferRepr(val id: Int, val courseId: Int, val termNumber: Int)

/**
 * Builds the Siren representations
 */
fun Programme.programmeToDetailRepr() =
    SirenBuilder(ShortProgrammeReprWithoutOffer(id, name, acronym, termSize))
        .entities(
            offers.map { offer -> this.buildSubentities(offer) }
        )
        .link("self", href = Uri.forProgrammesById(id))
        .link("up", href = Uri.forProgrammes())
        .toSiren()

private fun Programme.buildSubentities(offer: ProgrammeOffer): EmbeddedRepresentation =
    SirenBuilder(ShortOfferRepr(offer.id, offer.courseId, offer.termNumber))
        .klass("offer")
        .title("${offer.courseAcr} Offer")
        .rel(Uri.relProgrammeOffer)
        .link("self", href = Uri.forProgrammeOfferById(this.id, offer.id))
        .toEmbed()
