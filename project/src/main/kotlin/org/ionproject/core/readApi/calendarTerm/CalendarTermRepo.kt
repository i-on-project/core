package org.ionproject.core.readApi.calendarTerm

import org.ionproject.core.readApi.calendarTerm.model.CalendarTerm

interface CalendarTermRepo {
    fun getTerms(page: Int, limit: Int): List<CalendarTerm>
    fun getTermByCalId(calId: String, page: Int, limit: Int): CalendarTerm?
}