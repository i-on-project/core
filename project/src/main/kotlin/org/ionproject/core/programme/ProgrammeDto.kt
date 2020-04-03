package org.ionproject.core.programme

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.http.HttpMethod
import java.net.URI

/*
 * Builds the input model representation,
 * toProgramme invokes Programme entity constructor
 * to check if the object properties in the request
 * are valid.
 */
class ProgrammeInputModel() {
    fun toProgramme() = Unit
}

class ProgrammeOfferInputModel() {
    fun toProgrammeOffer() = Unit
}

/*
 * Builds the output model representations
 */
class ProgrammeOutputModel(private val programme : IProgramme) {
    fun toSirenObject() = SirenBuilder(programme)
            .entities(
                    programme.offers.map {
                        buildSubentities(programme, it)
                    }
            )
            .action(
                    Action(name = "edit-programme", title = "Edit Programme",
                            method = HttpMethod.PUT,
                            href = URI("${PROGRAMMES_PATH}/${programme.acronym}"),
                            type = JSON_MEDIA_TYPE,
                            fields = listOf(
                                    Field(name = "ProgrammeName", type = "text"),
                                    Field(name = "Acronym", type = "text"),
                                    Field(name = "TermSize", type = "number")
                            )
                    )
            )
            .action(
                    Action(name = "add-offer", title = "Add Offer", method = HttpMethod.POST,
                            href = URI("${PROGRAMMES_PATH}/${programme.acronym}/offers"),
                            type = JSON_MEDIA_TYPE,
                            fields = listOf(
                                    Field(name = "CourseAcronym", type = "text"),
                                    Field(name = "CurricularTerm", type = "number"),
                                    Field(name = "Precedents", type = "list"),
                                    Field(name = "Optional", type = "boolean")
                            )
                    )
            )
            .link("self", href = URI("${PROGRAMMES_PATH}/${programme.acronym}"))
            .link("up", href = URI("${PROGRAMMES_PATH}/"))
            .toSiren()

    private fun buildSubentities(programme: IProgramme, offer: IProgrammeOffer) : EmbeddedRepresentation = SirenBuilder()
            .klass("offer")
            .title("${offer.acronym} Offer")
            .rel(REL_PROGRAMME_OFFER)
            .link("self", URI("${PROGRAMMES_PATH}/${programme.acronym}/offers/${offer.id}"))
            .toEmbed()
}

class ProgrammesOutputModel(private val programmes : List<IProgramme>) {
    fun toSirenObject() = SirenBuilder()
            .klass("collection", "programme")
            .entities(programmes.map {
                buildSubentities(it)
            })
            .action(
                    Action(
                            name = "add-programme", title = "Add Programme", method = HttpMethod.POST,
                            href = URI(PROGRAMMES_PATH), type = JSON_MEDIA_TYPE,
                            fields = listOf(
                                        Field(name = "ProgrammeName", type = "text"),
                                        Field(name = "Acronym", type = "text"),
                                        Field(name = "TermSize", type = "text")
                                    )
                    )
            ).link("self", href = URI(PROGRAMMES_PATH))
            .toSiren()

    private fun buildSubentities(programme : IProgramme) = SirenBuilder()
            .klass("Programme")
            .rel("item")
            .link("self", URI("${PROGRAMMES_PATH}/${programme.acronym}"))
            .toEmbed()
}

class ProgrammeOfferOutputModel(private val offer : IProgrammeOffer) {
    fun toSirenObject() = SirenBuilder(offer)
            .klass("offer")
            .entities(
                    offer.precedents?.map {     //There is a chance that there are no subentities,
                        buildSubentities(it)    //sirenBuilder should be in charge of checking that
                    }
            )
            .action(
                    Action(
                            name = "edit", title = "edit offer", method = HttpMethod.PUT, type = JSON_MEDIA_TYPE,
                            href = URI("${COURSES_PATH}/${offer.acronym}"),
                            fields = listOf(
                                    Field(name = "Acronym", type = "text"),
                                    Field(name = "TermNumber", type = "number"),
                                    Field(name = "Credits", type = "number"),
                                    Field(name = "Optional", type = "boolean"),
                                    Field(name = "Precedents", type = "list")
                            )
                    )
            )
            .link("self", URI("${PROGRAMMES_PATH}/${offer.acronym}/offers/${offer.id}"))
            .link("related", URI("${PROGRAMMES_PATH}/${offer.acronym}"))
            .toSiren()

    private fun buildSubentities(course : ICourse) = SirenBuilder()
            .klass("course")
            .rel(REL_COURSE)
            .link("self", URI("${COURSES_PATH}/${course.acronym}"))
            .toEmbed()
}

