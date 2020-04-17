package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.representations.CalendarTermDetailRepr
import org.ionproject.core.calendarTerm.representations.CalendarTermListRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarTermController(private val calendarTermServices: CalendarTermServices) {

    @GetMapping(Uri.terms, produces = [Media.SIREN_TYPE])
    fun getTerms(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") limit: Int): Siren {
        return CalendarTermListRepr(calendarTermServices.getTerms(page, limit),page,limit)
    }

    @GetMapping(Uri.termByCalId, produces = [Media.SIREN_TYPE])
    fun getCalendarTerm(@PathVariable calTermId: String, @RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") limit: Int): Siren {
        return CalendarTermDetailRepr(calendarTermServices.getTermByCalId(calTermId, page, limit),page,limit)
    }
}