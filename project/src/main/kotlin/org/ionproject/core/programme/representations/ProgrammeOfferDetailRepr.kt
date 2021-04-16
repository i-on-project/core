package org.ionproject.core.programme.representations

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.ProgrammeOffer

/**
 * Output models
 */
data class ShortOfferForOfferRepr(val id: Int, val name: String, val acronym: String, val termNumber: List<Int>, val optional: Boolean)

data class ShortCourse(val id: Int)

/**
 * Siren representation generators
 */
fun ProgrammeOffer.offerToDetailRepr() =
    SirenBuilder(ShortOfferForOfferRepr(id, courseName, courseAcr, termNumber, optional))
        .klass("offer")
        .entities(
            SirenBuilder(ShortCourse(courseId))
                .klass("course")
                .rel(Uri.relCourse)
                .link("self", href = Uri.forCourseById(courseId))
                .toEmbed()
        ).link("self", href = Uri.forProgrammeOfferById(programmeId, id))
        .link(Uri.relProgramme, href = Uri.forProgrammesById(programmeId))
        .link(Uri.relOffers, href = Uri.forOffers(programmeId))
        .toSiren()
