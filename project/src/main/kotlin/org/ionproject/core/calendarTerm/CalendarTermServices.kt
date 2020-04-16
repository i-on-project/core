package org.ionproject.core.calendarTerm

import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.ionproject.core.common.model.CalendarTerm
import org.springframework.stereotype.Component

@Component
class CalendarTermServices(private val repo: CalendarTermRepoImpl) {
    fun getTerms(page: Int, limit: Int, defaultFlag: Boolean): List<CalendarTerm> {
        if (page < 0 || limit < 0)
            throw IncorrectParametersException("The parameters limit:$limit or page:$page, can't have negative values.")

        return repo.getTerms(page, limit, defaultFlag)
    }

    /*
     * Query Parameters tests should be moved from this place to a interceptor
     */
    fun getTermByCalId(calId: String, page: Int, limit: Int, defaultFlag: Boolean): CalendarTerm {
        if (page < 0 || limit < 0)
            throw IncorrectParametersException("The parameters limit:$limit or page:$page, can't have negative values.")

        return repo.getTermByCalId(calId, page, limit, defaultFlag)
    }
}