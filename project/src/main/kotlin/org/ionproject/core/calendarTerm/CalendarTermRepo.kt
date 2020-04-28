package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm

interface CalendarTermRepo {
    fun getTerms(page: Int, limit: Int): List<CalendarTerm>
    fun getTermByCalId(calId: String, page: Int, limit: Int): CalendarTerm
}