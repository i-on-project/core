package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val cousesQueryTemplate = "${version}/courses?page,limit"
    const val coursesWithParameters = "$version/courses?page={num}&limit={num2}"

    const val terms="$version/calendar-terms"
    const val termsQueryTemplate="$version/calendar-terms?page,limit"
    const val termsWithParameters = "$version/calendar-terms?page={num1}&limit={num2}"

    const val termByCalId= "$version/calendar-terms/{calTermId}"
    const val termsByCalIdQueryTemplate = "$version/calendar-termps/{calId}?page,limit"
    const val termsByCalIdWithQueryParams = "$version/calendar-terms/{calTermId}?page={num1}&limit={num2}"


    const val REL_CLASS = "/rel/class"
    const val REL_CLASS_SECTION = "/rel/class-section"
    const val REL_PROGRAMME_OFFER = "/rel/programmeOffer"
    const val REL_COURSE = "/rel/course"

    const val courses = "${version}/courses"
    const val courseById = "${version}/courses/{cid}"
    const val programmes = "${version}/programmes"
    const val programmesById = "${version}/programmes/{id}"
    const val klasses = "${version}/courses/{cid}/classes"
    const val klassByTerm = "${version}/courses/{cid}/classes/{calterm}"
    const val classSectionById = "${version}/courses/{cid}/classes/{calterm}/{sid}"
    const val programmeOfferById = "${version}/programmes/{idProgramme}/offers/{idOffer}"
    const val programmeByIdOffer = "${version}/programmes/{idProgramme}/offers/"
    const val calendarByClass = "${version}/courses/{cid}/classes/{calterm}/calendar"
    const val calendarByClassSection = "${version}/courses/{cid}/classes/{calterm}/{sid}/calendar"
    const val componentByCalendar =  "${version}/courses/{cid}/classes/{calterm}/calendar/{component}"



    val programmesByIdTemplate = UriTemplate(programmesById)
    val klassesTemplate = UriTemplate(klasses)
    val klassByTermTemplate = UriTemplate(klassByTerm)
    val classSectionByIdTemplate = UriTemplate(classSectionById)
    val programmeOfferByIdTemplate = UriTemplate(programmeOfferById)
    val programmeByIdOfferTemplate = UriTemplate(programmeByIdOffer)
    val courseByIdTemplate = UriTemplate(courseById)
    val coursesWithParametersTemplate = UriTemplate(coursesWithParameters)
    val calendarByClassTemplate = UriTemplate(calendarByClass)
    val calendarByClassSectionTemplate = UriTemplate(calendarByClassSection)
    val componentByCalendarTemplate = UriTemplate(componentByCalendar)

    val termsByCalIdWithQueryParamsTemplate = UriTemplate(termsByCalIdWithQueryParams)
    val termsWithParametersTemplate = UriTemplate(termsWithParameters)
    val termsByCalIdQueryTemplateTemplate = UriTemplate(termsByCalIdQueryTemplate)

    fun forCoursesTemplated() = URI(cousesQueryTemplate)
    fun forCourses() = URI(courses)
    fun forCourseById(courseId: Int) = courseByIdTemplate.expand(courseId)
    fun forCoursesWithParameters(page: Int, limit: Int) = coursesWithParametersTemplate.expand(page, limit)
    fun forKlasses(cid: Int) = klassesTemplate.expand(cid)
    fun forKlassByTerm(cid: Int, calterm: String) = klassByTermTemplate.expand(cid, calterm)
    fun forClassSectionById(cid: Int, calterm: String, sid: String) = classSectionByIdTemplate.expand(cid, calterm, sid)
    fun forCalendarByClass(cid: Int, calterm: String) = calendarByClassTemplate.expand(cid, calterm)
    fun forCalendarByClassSection(cid: Int, calterm: String, sid: String) = calendarByClassSectionTemplate.expand(cid, calterm, sid)
    fun forProgrammes() = URI(programmes)
    fun forProgrammesById(id: Int) = programmesByIdTemplate.expand(id)
    fun forProgrammeOfferById(idProgramme: Int, idOffer: Int) = programmeOfferByIdTemplate.expand(idProgramme, idOffer)
    fun forProgrammesByIdOffer(id: Int) = programmeByIdOfferTemplate.expand(id)
    fun forComponentByCalendar(courseId : Int,calTerm : String,componentId: String) = componentByCalendarTemplate.expand(courseId,calTerm,componentId)

    fun forTerms() = URI(terms)
    fun forTermsWithParamsTemplate() = URI(termsQueryTemplate)
    fun forTermsWithParams(page : Int=0, limit : Int=0) = termsWithParametersTemplate.expand(page,limit)

    fun forTermsByCalIdQuery(calTermId: String, page: Int = 0, limit: Int = 0) = termsByCalIdQueryTemplateTemplate.expand(calTermId,page,limit)
    fun forCalTermsWithParams(calTermId: String, page: Int = 0, limit: Int = 0) = termsByCalIdWithQueryParamsTemplate.expand(calTermId, page,limit)
}