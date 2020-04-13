package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.springframework.http.HttpMethod
import java.net.URI


fun programmesListRepr(programmes: List<IProgramme>) = SirenBuilder()
        .klass("collection", "programme")
        .entities(programmes.map {
            buildSubentities(it)
        })
        .action(
                Action(
                        name = "add-programme", title = "Add Programme", method = HttpMethod.POST,
                        href = URI(PROGRAMMES_PATH), type = JSON_MEDIA_TYPE,
                        fields = listOf(
                                Field(name = "ProgrammeAcr", type = "text"),
                                Field(name = "TermSize", type = "number")
                        )
                )
        ).link("self", href = URI(PROGRAMMES_PATH))
        .toSiren()

private fun buildSubentities(programme : IProgramme) =
        SirenBuilder(shortProgrammeRepr(programme.id, programme.acronym))
            .klass("Programme")
            .rel("item")
            .link("self", URI("$PROGRAMMES_PATH/${programme.id}"))
            .toEmbed()


data class shortProgrammeRepr(val programmeId: Int, val acr: String)