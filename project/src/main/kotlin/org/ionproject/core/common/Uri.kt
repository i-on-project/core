package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val courses = "${version}/courses"
    const val courseByAcr = "${version}/courses/{acr}"
    const val programmes = "${version}/programmes"
    const val programmesById = "${version}/programmes/{id}"
    const val klasses = "${version}/courses/{acr}/classes"
    const val klassByTerm = "${version}/courses/{acr}/classes/{calterm}"
    const val classSectionById = "${version}/courses/{acr}/classes/{calterm}/{id}"
    const val programmeOfferById = "$version/programmes/{idProgramme}/offers/{idOffer}"
    const val programmeByIdOffer = "$version/programmes/{idProgramme}/offers/"
    const val courseById = "${version}/courses/{id}"

    /*
     * REL'S, should they be here or other place?
     */
    const val REL_CLASS = "/rel/class"
    const val REL_PROGRAMME_OFFER = "/rel/programmeOffer"
    const val REL_COURSE = "/rel/course"

    val courseByAcrTemplate = UriTemplate(courseByAcr)
    val programmesByIdTemplate = UriTemplate(programmesById)
    val klassesTemplate = UriTemplate(klasses)
    val klassByTermTemplate = UriTemplate(klassByTerm)
    val classSectionByIdTemplate = UriTemplate(classSectionById)
    val programmeOfferByIdTemplate = UriTemplate(programmeOfferById)
    val programmeByIdOfferTemplate = UriTemplate(programmeByIdOffer)
    val courseByIdTemplate = UriTemplate(courseById)

    fun forCourses() = URI(courses)
    fun forProgrammes() = URI(programmes)
    fun forProgrammesById(id : Int) = programmesByIdTemplate.expand(id)
    fun forCourseByAcr(acr: String) = courseByAcrTemplate.expand(acr)   //TODO:DELETE
    fun forKlasses(acr: String) = klassesTemplate.expand(acr)
    fun forKlassByTerm(acr: String, calterm: String) = klassByTermTemplate.expand(acr, calterm)
    fun forClassSectionById(acr: String, calterm: String, id: String) = classSectionByIdTemplate.expand(acr, calterm, id)
    fun forProgrammeOfferById(idProgramme: Int, idOffer: Int) = programmeOfferByIdTemplate.expand(idProgramme, idOffer)
    fun forProgrammesByIdOffer(id: Int) = programmeByIdOfferTemplate.expand(id)
    fun forCourseById(courseId: Int) = courseByIdTemplate.expand(courseId)

}