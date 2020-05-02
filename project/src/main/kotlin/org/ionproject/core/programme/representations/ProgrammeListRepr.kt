package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.programme.model.Programme
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class ProgrammeReducedOutputModel(val programmeId: Int, val acr: String)

/**
 * Siren representation generators
 */
fun List<Programme>.programmesListRepr() = SirenBuilder()
    .klass("collection", "programme")
    .entities(this.map { programme -> programme.buildSubentities() })
    .action(
        Action(
            name = "add-programme",
            title = "Add Programme",
            method = HttpMethod.POST,
            href = Uri.forProgrammes().toTemplate(),
            type = Media.APPLICATION_JSON,
            fields = listOf(
                Field(name = "ProgrammeAcr", type = "text"),
                Field(name = "TermSize", type = "number")
            )
        )
    )
    .link("self", href = Uri.forProgrammes())
    .toSiren()

private fun Programme.buildSubentities() =
    SirenBuilder(ProgrammeReducedOutputModel(id, acronym))
        .klass("Programme")
        .rel("item")
        .link("self", Uri.forProgrammesById(id))
        .toEmbed()
