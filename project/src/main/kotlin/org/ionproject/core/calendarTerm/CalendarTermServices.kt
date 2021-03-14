package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.springframework.stereotype.Component

@Component
class CalendarTermServices(private val repo: CalendarTermRepoImpl) {
    fun getTerms(page: Int, limit: Int): List<CalendarTerm> {
        return repo.getTerms(page, limit)
    }

    /*
     * Query Parameters tests should be moved from this place to a interceptor
     */
    fun getTermByCalId(calId: String, page: Int, limit: Int): CalendarTerm? {
        return repo.getTermByCalId(calId, page, limit)
    }
}
