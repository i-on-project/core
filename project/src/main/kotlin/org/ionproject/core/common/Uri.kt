package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val courses = "${version}/courses"
    const val courseByAcr = "${version}/courses/{acr}"
    const val klasses = "$courseByAcr/classes"
    const val klassByTerm = "$klasses/{calterm}"
    const val classSectionById = "$klassByTerm/{id}"
    const val calendarByClass = "$klassByTerm/calendar"

    val courseByAcrTemplate = UriTemplate(courseByAcr)
    val klassesTemplate = UriTemplate(klasses)
    val klassByTermTemplate = UriTemplate(klassByTerm)
    val classSectionByIdTemplate = UriTemplate(classSectionById)
    val calendarByClassTemplate = UriTemplate(calendarByClass)

    fun forCourses() = URI(courses)
    fun forCourseByAcr(acr: String) = courseByAcrTemplate.expand(acr)
    fun forKlasses(acr: String) = klassesTemplate.expand(acr)
    fun forKlassByTerm(acr: String, calterm: String) = klassByTermTemplate.expand(acr, calterm)
    fun forClassSectionById(acr: String, calterm: String, id: String) = classSectionByIdTemplate.expand(acr, calterm, id)
    fun forCalendarByClass(acr: String, calterm: String) = calendarByClassTemplate.expand(acr, calterm)
}