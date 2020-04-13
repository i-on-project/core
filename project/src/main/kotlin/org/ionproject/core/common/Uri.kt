package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val courses = "${version}/courses"
    const val courseByAcr = "$courses/{course_id}"
    const val klasses = "$courseByAcr/classes"
    const val klassByTerm = "$klasses/{class_id}"
    const val classSectionById = "$klassByTerm/{classSection_id}"
    const val calendarByClass = "$klassByTerm/calendar"
    const val calendarByClassSection = "$classSectionById/calendar"


    val calendarByClassQueryParams = URI("$calendarByClass?type,startBefore,startAfter,endBefore,endAfter,summary")

    val CLASS_SECTION_REL = "/rel/class-section"

    val classSectionByIdTemplate = UriTemplate(classSectionById)
    val calendarByClassTemplate = UriTemplate(calendarByClass)
    val calendarByClassSectionTemplate = UriTemplate(calendarByClassSection)
    val klassByTermTemplate = UriTemplate(klassByTerm)

    fun forCalendarByClass(courseId: Int, classId: Int) = calendarByClassTemplate.expand(courseId, classId) //use withString class Id
    fun forCalendarByClassSection(courseId: Int, classId: Int, classSectionId: Int) =
        calendarByClassSectionTemplate.expand(courseId, classId, classSectionId)

    fun forClassesById(courseId: Int, id: String) = klassByTermTemplate.expand(courseId, id)
    fun forCalendarByClassWithString(courseId: Int, classId: String) = calendarByClassTemplate.expand(courseId, classId)
    fun forClassSectionById(courseId: Int, classId: String, classSectionId: String) = classSectionByIdTemplate.expand(courseId,classId,classSectionId)
}