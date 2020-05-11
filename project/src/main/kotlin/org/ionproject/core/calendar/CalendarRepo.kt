package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar

interface CalendarRepo {
    fun getClassCalendar(
            courseId: Int,
            calendarTerm: String,
            type: Char?,
            startBefore: String?,
            startAfter: String?,
            endBefore: String?,
            endAfter: String?,
            summary: String?
    ): Calendar?

    fun getClassSectionCalendar(
            courseId: Int,
            calendarTerm: String,
            classSectionId: String,
            type: Char?,
            startBefore: String?,
            startAfter: String?,
            endBefore: String?,
            endAfter: String?,
            summary: String?
    ): Calendar?

    fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar?
    fun getClassSectionCalendarComponent(
            courseId: Int,
            calendarTerm: String,
            classSectionId: String,
            componentId: Int
    ): Calendar?
}

