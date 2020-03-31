package org.ionproject.core.course_instance

import org.springframework.stereotype.Component

@Component
class CourseInstanceRepo {
    fun get(acr: String, calendarTerm: String): CourseInstance =
        CourseInstance(acr, calendarTerm) // for now

    fun getMany(acr: String): List<CourseInstance> =
        listOf(
            CourseInstance(acr, "1920v"),
            CourseInstance(acr, "1920i"),
            CourseInstance(acr, "1819v"))
}