package org.ionproject.core.programme.representations

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme

/**
 * Output models
 */
data class ProgrammeReducedOutputModel(val programmeId: Int, val name: String, val acronym: String)

/**
 * Siren representation generators
 */
fun List<Programme>.programmesListRepr(page: Int, limit: Int) = SirenBuilder()
    .klass("collection", "programmes")
    .entities(this.map { programme -> programme.buildSubentities() })
    .link("self", href = Uri.forPagingProgrammes(page, limit)).let {
        if (page > 0)
            it.link("previous", href = Uri.forPagingProgrammes(page - 1, limit))

        it
    }
    .link("next", href = Uri.forPagingProgrammes(page + 1, limit))
    .toSiren()

private fun Programme.buildSubentities() =
    SirenBuilder(ProgrammeReducedOutputModel(id, name, acronym))
        .klass("programme")
        .rel(Uri.relProgramme)
        .link("self", href = Uri.forProgrammesById(id))
        .toEmbed()
