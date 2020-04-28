package org.ionproject.core.programme.representations

import org.ionproject.core.common.*
import org.ionproject.core.programme.model.ProgrammeOffer
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class ShortOfferForOfferRepr(val id : Int, val acronym: String, val termNumber: Int, val optional: Boolean)

/**
 * Siren representation generators
 */
fun ProgrammeOffer.offerToDetailRepr() =
        SirenBuilder(ShortOfferForOfferRepr(id, courseAcr, termNumber, optional))
        .klass("offer")
        .entities(
                listOf(buildSubentities(courseId))
        )
        .action(
                Action(
                        name = "edit",
                        title = "edit offer",
                        method = HttpMethod.PUT,
                        type = Media.APPLICATION_JSON,
                        href = Uri.forProgrammeOfferById(programmeId, id).toTemplate(),
                        fields = listOf(
                                Field(name = "Acronym", type = "text"),
                                Field(name = "TermNumber", type = "number"),
                                Field(name = "Credits", type = "number"),
                                Field(name = "Optional", type = "boolean"),
                                Field(name = "Precedents", type = "list")
                        )
                )
        )
        .link("self", Uri.forProgrammeOfferById(programmeId, id))
        .link("related", Uri.forProgrammesById(id))
        .toSiren()

private fun buildSubentities(courseId : Int) = SirenBuilder()
        .klass("course")
        .rel(Uri.relCourse)
        .link("self", Uri.forCourseById(courseId))
        .toEmbed()
