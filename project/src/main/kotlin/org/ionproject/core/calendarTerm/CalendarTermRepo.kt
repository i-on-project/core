package org.ionproject.core.calendarTerm

import org.ionproject.core.common.model.CalendarTerm

interface CalendarTermRepo {
    fun getTerms(page: Int, limit: Int, defaultFlag: Boolean): List<CalendarTerm>
    fun getTermByCalId(calId: String, page: Int, limit: Int, defaultFlag: Boolean): CalendarTerm
}