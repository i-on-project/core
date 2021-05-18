package org.ionproject.core.programme

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.ionproject.core.programme.representations.OfferListProgramme
import org.ionproject.core.programme.representations.ProgrammeReducedOutputModel
import org.ionproject.core.programme.representations.ProgrammeRepresentation
import org.ionproject.core.programme.representations.ShortCourse
import org.ionproject.core.programme.representations.ShortOfferForOfferRepr
import org.ionproject.core.programme.representations.ShortOfferRepresentation
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

        val expected = SirenBuilder(ProgrammeRepresentation(p.id, p.name, p.acronym, p.termSize))
            .klass("programme")
            .entities(
                p.offers.map {
                    SirenBuilder(ShortOfferRepresentation(it.id, it.courseAcr, it.courseId, it.termNumber))
                        .klass("offer")
                        .rel(Uri.relProgrammeOffer)
                        .title(it.courseName)
                        .link("self", href = Uri.forProgrammeOfferById(it.programmeId, it.id))
                        .toEmbed()
                }
            )
            .link("self", href = Uri.forProgrammesById(p.id))
            .link("collection", href = Uri.forProgrammes())
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
            .klass("collection", "programme")
            .entities(
                list.map {
                    SirenBuilder(ProgrammeReducedOutputModel(it.id, it.name, it.acronym))
                        .klass("programme")
                        .rel("item")
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
            .klass("collection", "offer")
            .entities(
                list.map {
                    SirenBuilder(ShortOfferRepresentation(it.id, it.courseAcr, it.courseId, it.termNumber))
                        .klass("offer")
                        .rel("item")
                        .title(it.courseName)
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

        val expected = SirenBuilder(ShortOfferForOfferRepr(o.id, o.courseName, o.courseAcr, o.termNumber, o.optional))
            .klass("offer")
            .entities(
                SirenBuilder(ShortCourse(o.courseId))
                    .klass("course")
                    .rel(Uri.relCourse)
                    .link("self", href = Uri.forCourseById(o.courseId))
                    .toEmbed()
            ).link("self", href = Uri.forProgrammeOfferById(o.programmeId, o.id))
            .link(Uri.relProgramme, href = Uri.forProgrammesById(o.programmeId))
            .link(Uri.relOffers, href = Uri.forOffers(o.programmeId))
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
