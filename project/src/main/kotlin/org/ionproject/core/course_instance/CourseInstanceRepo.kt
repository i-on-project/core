package org.ionproject.core.course_instance

import org.springframework.stereotype.Component

@Component
class CourseInstanceRepo {
    fun get(acr: String, calendarTerm: String): FullCourseInstance =
        FullCourseInstance(acr, calendarTerm, "hey there") // for now

    fun getPage(acr: String, page: Int, size: Int): List<CourseInstance> =
        listOf(
            CourseInstance(acr, "1920v"),
            CourseInstance(acr, "1920i"),
            CourseInstance(acr, "1819v"))
}