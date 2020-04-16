package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val coursesWithParameters = "$version/courses?page={num}&limit={num2}"    //Used by the siren representation

    /*
     * REL'S, should they be here or other place?
     */
    const val REL_CLASS = "/rel/class"
    const val REL_PROGRAMME_OFFER = "/rel/programmeOffer"
    const val REL_COURSE = "/rel/course"

    const val courses = "${version}/courses"
    const val courseById = "${version}/courses/{cid}"
    const val programmes = "${version}/programmes"
    const val programmesById = "${version}/programmes/{id}"
    const val klasses = "${version}/courses/{cid}/classes"
    const val klassByTerm = "${version}/courses/{cid}/classes/{calterm}"
    const val classSectionById = "${version}/courses/{cid}/classes/{calterm}/{sid}"
    const val programmeOfferById = "$version/programmes/{idProgramme}/offers/{idOffer}"
    const val programmeByIdOffer = "$version/programmes/{idProgramme}/offers/"
    const val pagingQuery = "{?page,limit}"

    val programmesByIdTemplate = UriTemplate(programmesById)
    val klassesTemplate = UriTemplate(klasses)
    val klassByTermTemplate = UriTemplate(klassByTerm)
    val classSectionByIdTemplate = UriTemplate(classSectionById)
    val programmeOfferByIdTemplate = UriTemplate(programmeOfferById)
    val programmeByIdOfferTemplate = UriTemplate(programmeByIdOffer)
    val courseByIdTemplate = UriTemplate(courseById)
    val coursesWithParametersTemplate = UriTemplate(coursesWithParameters)

    fun forCourses() = URI(courses)
    fun forCourseById(courseId: Int) = courseByIdTemplate.expand(courseId)
    fun forCoursesWithParameters(page : Int, limit : Int) = coursesWithParametersTemplate.expand(page, limit)

    fun forKlasses(cid: Int) = klassesTemplate.expand(cid)
    fun forKlassByTerm(cid: Int, calterm: String) = klassByTermTemplate.expand(cid, calterm)
    fun forClassSectionById(cid: Int, calterm: String, sid: String) = classSectionByIdTemplate.expand(cid, calterm, sid)

    fun forProgrammes() = URI(programmes)
    fun forProgrammesById(id : Int) = programmesByIdTemplate.expand(id)
    fun forProgrammeOfferById(idProgramme: Int, idOffer: Int) = programmeOfferByIdTemplate.expand(idProgramme, idOffer)
    fun forProgrammesByIdOffer(id: Int) = programmeByIdOfferTemplate.expand(id)
}
