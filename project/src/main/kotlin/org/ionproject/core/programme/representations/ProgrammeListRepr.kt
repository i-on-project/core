package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.programme.model.Programme
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class ProgrammeReducedOutputModel(val programmeId: Int, val acronym: String)

/**
 * Siren representation generators
 */
fun List<Programme>.programmesListRepr() = SirenBuilder()
    .klass("collection", "programme")
    .entities(this.map { programme -> programme.buildSubentities() })
    .link("self", href = Uri.forProgrammes())
    .toSiren()

private fun Programme.buildSubentities() =
    SirenBuilder(ProgrammeReducedOutputModel(id, acronym))
        .klass("programme")
        .rel("item")
        .link("self", href = Uri.forProgrammesById(id))
        .toEmbed()
