package org.ionproject.core.programme

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.ionproject.core.programme.representations.OfferListProgramme
import org.ionproject.core.programme.representations.ProgrammeReducedOutputModel
import org.ionproject.core.programme.representations.ShortOfferRepr
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test

internal class ProgrammeControllerTest : ControllerTester() {

    companion object {
        fun getProgramme(): Programme {
            val pid = 1
            return Programme(
                pid,
                "licenciatura eng. inf.",
                "LEIC",
                6,
                mutableListOf(
                    ProgrammeOffer(1, "WAD", "Web Applications Development", pid, 2, listOf(6), true),
                    ProgrammeOffer(2, "SL", "Software Laboratory", pid, 1, listOf(6), false),
                    ProgrammeOffer(3, "DM", "Discrete Mathematics", pid, 3, listOf(1), false)
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

        data class OutputModel(val id: Int, val name: String, val acronym: String, val termSize: Int)

        val expected = SirenBuilder(OutputModel(p.id, p.name, p.acronym, p.termSize))
            .klass("programme")
            .link("self", href = Uri.forProgrammesById(p.id))
            .link(Uri.relProgrammes, href = Uri.forProgrammes())
            .link(Uri.relOffers, href = Uri.forOffers(p.id))
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getProgrammeCollection_shouldRespondWithTheExactSirenRepresentationOfProgrammeCollection() {
        val list = getProgrammeCollection()
        val selfHref = Uri.forPagingProgrammes(0, 10)

        val expected = SirenBuilder()
            .klass("collection", "programmes")
            .entities(
                list.map {
                    SirenBuilder(ProgrammeReducedOutputModel(it.id, it.name, it.acronym))
                        .klass("programme")
                        .rel(Uri.relProgramme)
                        .link("self", href = Uri.forProgrammesById(it.id))
                        .toEmbed()
                }
            )
            .link("self", href = Uri.forPagingProgrammes(0, 10))
            .link("next", href = Uri.forPagingProgrammes(1, 10))
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getProgrammeCollection_shouldRespondWithTheExactSirenRepresentationOfProgrammeOfferCollection() {
        val p = getProgramme()
        val list = p.offers
        val selfHref = Uri.forPagingOffers(p.id, 0, 10)

        val expected = SirenBuilder(OfferListProgramme(p.id))
            .klass("collection", "offers")
            .entities(
                list.map {
                    SirenBuilder(ShortOfferRepr(it.id, it.courseName, it.courseId, it.termNumber))
                        .title(it.courseName)
                        .rel(Uri.relProgrammeOffer)
                        .link("self", href = Uri.forProgrammeOfferById(it.programmeId, it.id))
                        .toEmbed()
                }
            )
            .link("self", href = Uri.forPagingOffers(p.id, 0, 10))
            .link("next", href = Uri.forPagingOffers(p.id, 1, 10))
            .link(Uri.relProgramme, href = Uri.forProgrammesById(p.id))
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

        data class OutputModel(val id: Int, val name: String, val acronym: String, val termNumber: List<Int>, val optional: Boolean)
        data class ShortCourse(val courseId: Int, val courseName: String, val courseAcr: String)

        val expected = SirenBuilder(OutputModel(o.id, o.courseName, o.courseAcr, o.termNumber, o.optional))
            .klass("offer")
            .entities(
                SirenBuilder(ShortCourse(o.courseId, o.courseName, o.courseAcr))
                    .klass("course")
                    .rel(Uri.relCourse)
                    .link("self", href = Uri.forCourseById(o.courseId))
                    .toEmbed()
            )
            .link("self", href = Uri.forProgrammeOfferById(o.programmeId, o.id))
            .link(Uri.relOffers, href = Uri.forOffers(o.programmeId))
            .link(Uri.relProgramme, href = Uri.forProgrammesById(o.programmeId))
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
