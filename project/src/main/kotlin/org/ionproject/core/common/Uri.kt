package org.ionproject.core.common

import org.ionproject.core.search.model.SearchQuery
import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    const val version = "/v0"
    const val rfcPagingQuery = "{?page,limit}"
    const val springWebPagingQuery = "?page={page}&limit={limit}"

    // Calendar Terms
    const val calendarTerms = "$version/calendar-terms"
    const val calendarTermById = "$version/calendar-terms/{calterm}"

    val calendarTermByIdTemplate = UriTemplate(calendarTermById)
    val pagingCalendarTerms = UriTemplate("${calendarTerms}${rfcPagingQuery}")
    val pagingCalendarTermById = UriTemplate("${calendarTermById}${rfcPagingQuery}")

    fun forCalTerms() = URI(calendarTerms)
    fun forCalTermById(calterm: String) = calendarTermByIdTemplate.expand(calterm)
    fun forPagingCalTerms(page: Int, limit: Int) =
        UriTemplate("${calendarTerms}${springWebPagingQuery}").expand(page, limit)

    fun forPagingCalTermById(calterm: String, page: Int, limit: Int) =
        UriTemplate("${calendarTermById}${springWebPagingQuery}").expand(calterm, page, limit)

    // Courses
    const val courses = "${version}/courses"
    const val courseById = "${version}/courses/{cid}"

    val courseByIdTemplate = UriTemplate(courseById)
    val pagingCourses = UriTemplate("${courses}${rfcPagingQuery}")

    fun forCourses() = URI(courses)
    fun forCourseById(courseId: Int) = courseByIdTemplate.expand(courseId)
    fun forPagingCourses(page: Int, limit: Int) = UriTemplate("${courses}${springWebPagingQuery}").expand(page, limit)

    // Programmes
    const val programmes = "${version}/programmes"
    const val programmesById = "${version}/programmes/{id}"
    const val programmeOfferById = "$version/programmes/{idProgramme}/offers/{idOffer}"
    const val programmeByIdOffer = "$version/programmes/{idProgramme}/offers/"

    val programmesByIdTemplate = UriTemplate(programmesById)
    val programmeOfferByIdTemplate = UriTemplate(programmeOfferById)
    val programmeByIdOfferTemplate = UriTemplate(programmeByIdOffer)

    fun forProgrammes() = URI(programmes)
    fun forProgrammesById(id: Int) = programmesByIdTemplate.expand(id)
    fun forProgrammeOfferById(idProgramme: Int, idOffer: Int) = programmeOfferByIdTemplate.expand(idProgramme, idOffer)
    fun forProgrammesByIdOffer(id: Int) = programmeByIdOfferTemplate.expand(id)

    // Classes
    const val klasses = "${version}/courses/{cid}/classes"
    const val klassByCalTerm = "${version}/courses/{cid}/classes/{calterm}"

    val klassesTemplate = UriTemplate(klasses)
    val klassByCalTermTemplate = UriTemplate(klassByCalTerm)
    val pagingKlassesTemplate = UriTemplate("${klasses}${rfcPagingQuery}")

    fun forKlasses(cid: Int) = klassesTemplate.expand(cid)
    fun forKlassByCalTerm(cid: Int, calterm: String) = klassByCalTermTemplate.expand(cid, calterm)
    fun forPagingKlass(cid: Int, page: Int, limit: Int) =
        UriTemplate("${klasses}${springWebPagingQuery}").expand(cid, page, limit)

    // Class Sections
    const val classSectionById = "${version}/courses/{cid}/classes/{calterm}/{sid}"

    val classSectionByIdTemplate = UriTemplate(classSectionById)

    fun forClassSectionById(cid: Int, calterm: String, sid: String) = classSectionByIdTemplate.expand(cid, calterm, sid)

    // Calendars
    const val calendarByClass = "${version}/courses/{cid}/classes/{calterm}/calendar"
    const val calendarByClassSection = "${version}/courses/{cid}/classes/{calterm}/{sid}/calendar"
    const val componentByClassCalendar = "${version}/courses/{cid}/classes/{calterm}/calendar/{cmpid}"
    const val componentByClassSectionCalendar = "${version}/courses/{cid}/classes/{calterm}/{sid}/calendar/{cmpid}"

    val calendarByClassTemplate = UriTemplate(calendarByClass)
    val calendarByClassSectionTemplate = UriTemplate(calendarByClassSection)
    val componentByClassCalendarTemplate = UriTemplate(componentByClassCalendar)
    val componentByClassSectionCalendarTemplate = UriTemplate(componentByClassSectionCalendar)

    fun forCalendarByClass(cid: Int, calterm: String) = calendarByClassTemplate.expand(cid, calterm)
    fun forCalendarByClassSection(cid: Int, calterm: String, sid: String) =
        calendarByClassSectionTemplate.expand(cid, calterm, sid)

    fun forCalendarComponentByClass(cid: Int, calterm: String, cmpid: String) =
        componentByClassCalendarTemplate.expand(cid, calterm, cmpid)

    fun forCalendarComponentByClassSection(cid: Int, calterm: String, sid: String, cmpid: String) =
        componentByClassSectionCalendarTemplate.expand(cid, calterm, sid, cmpid)


    // Search
    const val search = "$version/search"
    val searchTemplate = UriTemplate("$search?query={query}&types={types}&limit={limit}&page={page}")
    fun forSearch(query: SearchQuery) : URI = searchTemplate.expand(query.query, query.types.joinToString(","), query.limit, query.page)



    // custom link rel
    const val relClass = "/rel/class"
    const val relClassSection = "/rel/class-section"
    const val relProgrammeOffer = "/rel/programmeOffer"
    const val relCourse = "/rel/course"
    const val relCalendar = "/rel/calendar"
}
