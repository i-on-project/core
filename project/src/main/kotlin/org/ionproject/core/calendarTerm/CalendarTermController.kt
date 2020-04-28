package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.representations.toCalendarTermDetailRepr
import org.ionproject.core.calendarTerm.representations.toCalendarTermListRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarTermController(private val calendarTermServices: CalendarTermServices) {

    @GetMapping(Uri.calendarTerms, produces = [Media.SIREN_TYPE])
    fun getTerms(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") limit: Int): Siren {
        return calendarTermServices.getTerms(page, limit).toCalendarTermListRepr(page,limit)
    }

    @GetMapping(Uri.calendarTermById, produces = [Media.SIREN_TYPE])
    fun getCalendarTerm(@PathVariable calterm: String,
                        @RequestParam(defaultValue = "0") page: Int,
                        @RequestParam(defaultValue = "10") limit: Int): Siren {
        return calendarTermServices.getTermByCalId(calterm, page, limit).toCalendarTermDetailRepr(page,limit)
    }
}