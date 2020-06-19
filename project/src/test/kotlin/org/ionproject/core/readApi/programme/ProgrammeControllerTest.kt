package org.ionproject.core.readApi.programme

import org.ionproject.core.readApi.common.*
import org.ionproject.core.readApi.programme.model.Programme
import org.ionproject.core.readApi.programme.model.ProgrammeOffer
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class ProgrammeControllerTest : ControllerTester() {

    companion object {
        fun getProgramme(): Programme {
            val pid = 1
            return Programme(
                pid, "licenciatura eng. inf.", "LEIC", 6, mutableListOf(
                    ProgrammeOffer(1, "WAD", pid, 2, 3, true),
                    ProgrammeOffer(2, "SL", pid, 1, 3, false),
                    ProgrammeOffer(3, "DM", pid, 3, 1, false)
                )
            )
        }

        fun getProgrammeCollection() = listOf(
            Programme(1, "licenciatura eng. inf.", "LEIC", 6, mutableListOf()),
            Programme(2, "mestrado eng. inf.", "MEIC", 4, mutableListOf())
        )
    }

    @Test
    fun getProgramme_shouldRespondWithTheExactSirenRepresentationOfProgramme() {
        val p = getProgramme()
        val selfHref = Uri.forProgrammesById(p.id)

        data class OutputModel(val id: Int, val name: String? = null, val acronym: String, val termSize: Int)
        data class ItemOutputModel(val id: Int, val courseId: Int, val termNumber: Int)

        val expected = SirenBuilder(OutputModel(p.id, p.name, p.acronym, p.termSize))
            .entities(p.offers.map {
                SirenBuilder(ItemOutputModel(it.id, it.courseId, it.termNumber))
                    .klass("offer")
                    .title("${it.courseAcr} Offer")
                    .rel(Uri.relProgrammeOffer)
                    .link("self", href = Uri.forProgrammeOfferById(it.programmeId, it.id))
                    .toEmbed()
            })
            .action(
                Action(
                    name = "edit-programme",
                    title = "Edit Programme",
                    method = HttpMethod.PUT,
                    href = Uri.forProgrammesById(p.id).toTemplate(),
                    type = Media.APPLICATION_JSON,
                    fields = listOf(
                        Field(
                            name = "ProgrammeName",
                            type = "text"
                        ),   //name may be null, but the action allows to modify
                        Field(name = "Acronym", type = "text"),
                        Field(name = "TermSize", type = "number")
                    )
                )
            )
            .action(
                Action(
                    name = "add-offer",
                    title = "Add Offer",
                    method = HttpMethod.POST,
                    href = Uri.forProgrammesByIdOffer(p.id).toTemplate(),
                    type = Media.APPLICATION_JSON,
                    fields = listOf(
                        Field(name = "CourseId", type = "number"),
                        Field(name = "CurricularTerm", type = "number"),
                        Field(name = "Optional", type = "boolean")
                    )
                )
            )
            .link("self", href = Uri.forProgrammesById(p.id))
            .link("up", href = Uri.forProgrammes())
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getProgrammeCollection_shouldRespondWithTheExactSirenRepresentationOfProgrammeCollection() {
        val list = getProgrammeCollection()
        val selfHref = Uri.forProgrammes()

        data class OutputModel(val programmeId: Int, val acronym: String)

        val expected = SirenBuilder()
            .klass("collection", "programme")
            .entities(list.map { programme ->
                SirenBuilder(OutputModel(programme.id, programme.acronym))
                    .klass("programme")
                    .rel("item")
                    .link("self", href = Uri.forProgrammesById(programme.id))
                    .toEmbed()
            })
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

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getProgrammeOffer_shouldRespondWithTheExactSirenRepresentationOfProgrammeOffer() {
        val p = getProgramme()
        val o = p.offers[0]
        val selfHref = Uri.forProgrammeOfferById(o.programmeId, o.id)

        data class OutputModel(val id: Int, val acronym: String, val termNumber: Int, val optional: Boolean)

        val expected = SirenBuilder(OutputModel(o.id, o.courseAcr, o.termNumber, o.optional))
            .klass("offer")
            .entities(
                SirenBuilder()
                    .klass("course")
                    .rel(Uri.relCourse)
                    .link("self", href = Uri.forCourseById(o.courseId))
                    .toEmbed()
            )
            .action(
                Action(
                    name = "edit",
                    title = "edit offer",
                    method = HttpMethod.PUT,
                    type = Media.APPLICATION_JSON,
                    href = selfHref.toTemplate(),
                    fields = listOf(
                        Field(name = "Acronym", type = "text"),
                        Field(name = "TermNumber", type = "number"),
                        Field(name = "Optional", type = "boolean")
                    )
                )
            )
            .link("self", href = selfHref)
            .link("related", href = Uri.forProgrammesById(o.programmeId))
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}