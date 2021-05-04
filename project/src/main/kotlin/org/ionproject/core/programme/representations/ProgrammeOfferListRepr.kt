package org.ionproject.core.programme.representations

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.ProgrammeOffer

data class OfferListProgramme(val programmeId: Int)

fun List<ProgrammeOffer>.programmeToListRepr(id: Int, page: Int, limit: Int) =
    SirenBuilder(OfferListProgramme(id))
        .klass("collection", "offer")
        .entities(map { it.buildSubEntities() })
        .link("self", href = Uri.forPagingOffers(id, page, limit)).let {
            if (page > 0)
                it.link("previous", href = Uri.forPagingOffers(id, page - 1, limit))

            it
        }
        .link("next", href = Uri.forPagingOffers(id, page + 1, limit))
        .link(Uri.relProgramme, href = Uri.forProgrammesById(id))
        .toSiren()

private fun ProgrammeOffer.buildSubEntities() =
    SirenBuilder(ShortOfferRepresentation(id, courseAcr, courseId, termNumber))
        .klass("offer")
        .rel("item")
        .title(courseName)
        .link("self", href = Uri.forProgrammeOfferById(programmeId, id))
        .toEmbed()
