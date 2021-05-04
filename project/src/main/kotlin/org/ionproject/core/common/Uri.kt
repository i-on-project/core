package org.ionproject.core.common

import org.ionproject.core.search.model.SearchQuery
import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {

    var baseUrl = System.getenv("ION_CORE_BASE_URL")

    const val error = "/error"
    const val version = "/v0"
    const val rfcPagingQuery = "{?page,limit}"
    const val springWebPagingQuery = "?page={page}&limit={limit}"

    // Calendar Terms
    const val calendarTerms = "$version/calendar-terms"
    const val calendarTermById = "$version/calendar-terms/{calterm}"

    val calendarTermByIdTemplate = UriTemplate(calendarTermById)
    val pagingCalendarTerms = UriTemplate("${calendarTerms}$rfcPagingQuery")
    val pagingCalendarTermById = UriTemplate("${calendarTermById}$rfcPagingQuery")

    fun forCalTerms() = URI(calendarTerms)
    fun forCalTermById(calterm: String) = calendarTermByIdTemplate.expand(calterm)
    fun forPagingCalTerms(page: Int, limit: Int) =
        UriTemplate("${calendarTerms}$springWebPagingQuery").expand(page, limit)

    fun forPagingCalTermById(calterm: String, page: Int, limit: Int) =
        UriTemplate("${calendarTermById}$springWebPagingQuery").expand(calterm, page, limit)

    // Courses
    const val courses = "$version/courses"
    const val courseById = "$version/courses/{cid}"

    val courseByIdTemplate = UriTemplate(courseById)
    val pagingCourses = UriTemplate("${courses}$rfcPagingQuery")

    fun forCourses() = URI(courses)
    fun forCourseById(courseId: Int) = courseByIdTemplate.expand(courseId)
    fun forPagingCourses(page: Int, limit: Int) = UriTemplate("${courses}$springWebPagingQuery").expand(page, limit)

    // Programmes
    const val programmes = "$version/programmes"
    const val programmesById = "$version/programmes/{id}"
    const val programmeOfferById = "$version/programmes/{idProgramme}/offers/{idOffer}"
    const val programmeByIdOffer = "$version/programmes/{id}/offers"

    val programmesByIdTemplate = UriTemplate(programmesById)

    val programmeOfferByIdTemplate = UriTemplate(programmeOfferById)
    val pagingProgrammes = UriTemplate("${programmes}$rfcPagingQuery")

    fun forProgrammes() = URI(programmes)
    fun forPagingProgrammes(page: Int, limit: Int) = UriTemplate("${programmes}$springWebPagingQuery").expand(page, limit)
    fun forProgrammesById(id: Int) = programmesByIdTemplate.expand(id)
    fun forProgrammeOfferById(idProgramme: Int, idOffer: Int) = programmeOfferByIdTemplate.expand(idProgramme, idOffer)
    fun forOffers(id: Int) = UriTemplate(programmeByIdOffer).expand(id)
    fun forPagingOffers(id: Int, page: Int, limit: Int) = UriTemplate("${programmeByIdOffer}$springWebPagingQuery")
        .expand(id, page, limit)

    // Classes
    const val klasses = "$version/courses/{cid}/classes"
    const val klassByCalTerm = "$version/courses/{cid}/classes/{calterm}"

    val klassesTemplate = UriTemplate(klasses)
    val klassByCalTermTemplate = UriTemplate(klassByCalTerm)
    val pagingKlassesTemplate = UriTemplate("${klasses}$rfcPagingQuery")

    fun forKlasses(cid: Int) = klassesTemplate.expand(cid)
    fun forKlassByCalTerm(cid: Int, calterm: String) = klassByCalTermTemplate.expand(cid, calterm)
    fun forPagingKlass(cid: Int, page: Int, limit: Int) =
        UriTemplate("${klasses}$springWebPagingQuery").expand(cid, page, limit)

    // Class Sections
    const val classSectionById = "$version/courses/{cid}/classes/{calterm}/{sid}"

    val classSectionByIdTemplate = UriTemplate(classSectionById)

    fun forClassSectionById(cid: Int, calterm: String, sid: String) = classSectionByIdTemplate.expand(cid, calterm, sid)

    // Calendars
    const val calendarByClass = "$version/courses/{cid}/classes/{calterm}/calendar"
    const val calendarByClassSection = "$version/courses/{cid}/classes/{calterm}/{sid}/calendar"
    const val componentByClassCalendar = "$version/courses/{cid}/classes/{calterm}/calendar/{cmpid}"
    const val componentByClassSectionCalendar = "$version/courses/{cid}/classes/{calterm}/{sid}/calendar/{cmpid}"

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

    // Access Control
    const val revokeToken = "/revokeToken"
    const val issueToken = "/issueToken"
    const val importClassCalendar = "$version/import/courses/{cid}/classes/{calterm}/calendar"
    const val importClassSectionCalendar = "$version/import/courses/{cid}/classes/{calterm}/{sid}/calendar"

    val importClassCalendarTemplate = UriTemplate(importClassCalendar)
    val importClassSectionCalendarTemplate = UriTemplate(importClassSectionCalendar)

    fun forImportClassCalendar(cid: Int, calterm: String) =
        importClassCalendarTemplate.expand(cid, calterm)

    fun forImportClassSectionCalendar(cid: Int, calterm: String, sid: String) =
        importClassSectionCalendarTemplate.expand(cid, calterm, sid)

    // Search
    const val search = "$version/search"
    val searchTemplate = UriTemplate("$search?query={query}&types={types}&limit={limit}&page={page}")
    val pagingSearch = UriTemplate("$search{?query,types,limit,page}")
    fun forSearch(query: SearchQuery): URI =
        searchTemplate.expand(query.query, query.types.joinToString(","), query.limit, query.page)

    // custom link rel
    const val relClass = "/rel/class"
    const val relClassSection = "/rel/class-section"
    const val relProgrammeOffer = "/rel/programmeOffer"
    const val relCourse = "/rel/course"
    const val relCalendar = "/rel/calendar"
    const val relProgramme = "/rel/programme"
    const val relProgrammes = "/rel/programmes"
    const val relOffers = "/rel/offers"
}
