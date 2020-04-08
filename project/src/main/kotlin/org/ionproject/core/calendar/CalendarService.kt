package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent

class CalendarService {
    private val repo = CalendarRepository()

    fun getClassCalendarComponent(classId: String, compId: String): CalendarComponent? = getClassCalendar(classId)?.find {
        it.uid.value.equals(compId)
    }

    fun getClassCalendar(classId: String): Calendar? = repo[CalendarRepository.CLASS]?.get(classId)
}