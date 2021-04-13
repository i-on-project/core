package org.ionproject.core.programme.representations

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme

/**
 * Output models
 */
data class ShortProgrammeReprWithoutOffer(val id: Int, val name: String, val acronym: String, val termSize: Int)

/**
 * Builds the Siren representations
 */
fun Programme.programmeToDetailRepr() =
    SirenBuilder(ShortProgrammeReprWithoutOffer(id, name, acronym, termSize))
        .klass("programme")
        .link("self", href = Uri.forProgrammesById(id))
        .link(Uri.relProgrammes, href = Uri.forProgrammes())
        .link(Uri.relOffers, href = Uri.forOffers(id))
        .toSiren()
