package org.ionproject.core.programme.representations

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer

/**
 * Output models
 */
data class ProgrammeRepresentation(val id: Int, val name: String, val acronym: String, val termSize: Int)
data class ShortOfferRepresentation(val id: Int, val acronym: String, val courseId: Int, val termNumber: List<Int>)

/**
 * Builds the Siren representations
 */
fun Programme.programmeToDetailRepr() =
    SirenBuilder(ProgrammeRepresentation(id, name, acronym, termSize))
        .klass("programme")
        .entities(offers.map { it.buildSubEntities() })
        .link("self", href = Uri.forProgrammesById(id))
        .link("collection", href = Uri.forProgrammes())
        .link(Uri.relOffers, href = Uri.forOffers(id))
        .toSiren()

private fun ProgrammeOffer.buildSubEntities() =
    SirenBuilder(ShortOfferRepresentation(id, courseAcr, courseId, termNumber))
        .klass("offer")
        .rel(Uri.relProgrammeOffer)
        .title(courseName)
        .link("self", href = Uri.forProgrammeOfferById(programmeId, id))
        .toEmbed()
