package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.springframework.util.MultiValueMap

interface CalendarRepo {
    fun getClassCalendar(
        courseId: Int,
        calendarTerm: String,
        filters: MultiValueMap<String, String>
    ): Calendar?

    fun getClassSectionCalendar(
        courseId: Int,
        calendarTerm: String,
        classSectionId: String,
        filters: MultiValueMap<String, String>
    ): Calendar?

    fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar?

    fun getClassSectionCalendarComponent(
        courseId: Int,
        calendarTerm: String,
        classSectionId: String,
        componentId: Int
    ): Calendar?
}