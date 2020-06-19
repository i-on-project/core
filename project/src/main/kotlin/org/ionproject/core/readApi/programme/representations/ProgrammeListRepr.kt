package org.ionproject.core.readApi.programme.representations

import org.ionproject.core.readApi.common.*
import org.ionproject.core.readApi.programme.model.Programme
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
        .klass("programme")
        .rel("item")
        .link("self", href = Uri.forProgrammesById(id))
        .toEmbed()
