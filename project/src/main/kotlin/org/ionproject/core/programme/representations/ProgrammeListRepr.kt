package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.Programme
import org.springframework.http.HttpMethod


fun programmesListRepr(programmes: List<Programme>) = SirenBuilder()
        .klass("collection", "programme")
        .entities(programmes.map {
            buildSubentities(it)
        })
        .action(
                Action(
                        name = "add-programme",
                        title = "Add Programme",
                        method = HttpMethod.POST,
                        href = Uri.forProgrammes(),
                        type = Media.APPLICATION_JSON,
                        fields = listOf(
                                Field(name = "ProgrammeAcr", type = "text"),
                                Field(name = "TermSize", type = "number")
                        )
                )
        )
        .link("self", href = Uri.forProgrammes())
        .toSiren()

private fun buildSubentities(programme : Programme) =
        SirenBuilder(shortProgrammeRepr(programme.id, programme.acronym))
            .klass("Programme")
            .rel("item")
            .link("self", Uri.forProgrammesById(programme.id))
            .toEmbed()


data class shortProgrammeRepr(val programmeId: Int, val acr: String)