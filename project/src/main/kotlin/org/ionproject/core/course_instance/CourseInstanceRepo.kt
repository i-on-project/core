package org.ionproject.core.course_instance

import org.ionproject.core.class_section.ClassSection
import org.springframework.stereotype.Component

@Component
class CourseInstanceRepo {
    fun get(acr: String, calendarTerm: String): FullCourseInstance =
        FullCourseInstance(
                acr,
                calendarTerm,
                listOf(ClassSection(acr, calendarTerm, "1d"), ClassSection(acr, calendarTerm, "2d")))

    fun getPage(acr: String, page: Int, size: Int): List<CourseInstance> =
        listOf(
            CourseInstance(acr, "1920v"),
            CourseInstance(acr, "1920i"),
            CourseInstance(acr, "1819v"))
}