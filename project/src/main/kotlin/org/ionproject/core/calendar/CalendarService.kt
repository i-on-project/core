package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent

class CalendarService {
    private val repo = CalendarRepository()

    fun getClassCalendarComponent(classId: Int, compId: String): CalendarComponent? = getClassCalendar(classId)?.find {
        it.uid.value.equals(compId)
    }

    fun getClassCalendar(classId: Int): Calendar? = repo[CalendarRepository.CLASS]?.get(classId)
}