package org.ionproject.core.common

import org.springframework.web.util.UriTemplate

object Uri {
    private const val version = "/v0"

    const val courses = "${version}/courses"
    const val courseByAcr = "$courses/{course_id}"
    const val klasses = "$courseByAcr/classes"
    const val klassByTerm = "$klasses/{class_id}"
    const val classSectionById = "$klassByTerm/{classSection_id}"
    const val calendarByClass = "$klassByTerm/calendar"
    const val calendarByClassSection = "$classSectionById/calendar"

    val calendarByClassTemplate = UriTemplate(calendarByClass)
    val calendarByClassSectionTemplate = UriTemplate(calendarByClassSection)

    fun forCalendarByClass(courseId: Int, classId: Int) = calendarByClassTemplate.expand(courseId, classId)
    fun forCalendarByClassSection(courseId: Int, classId: Int, classSectionId: Int) = calendarByClassSectionTemplate.expand(courseId, classId, classSectionId)
}