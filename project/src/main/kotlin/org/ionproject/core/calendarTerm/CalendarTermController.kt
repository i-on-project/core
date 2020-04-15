package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.representations.CalendarTermDetailRepr
import org.ionproject.core.calendarTerm.representations.CalendarTermListRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarTermController(private val calendarTermServices: CalendarTermServices) {

    @GetMapping(Uri.terms)
    fun getTerms(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "0") limit: Int): ResponseEntity<Siren> {
        val defaultFlag = page == 0 && limit == 0

        return calendarTermServices.getTerms(page, limit, defaultFlag)
                .let {
                    ResponseEntity.ok()
                            .header("Content-Type", Media.SIREN_TYPE)
                            .body(CalendarTermListRepr(it, page, limit))
                }
    }

    @GetMapping(Uri.termByCalId)
    fun getCalendarTerm(@PathVariable calTermId: String, @RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "0") limit: Int): ResponseEntity<Siren> {
        val defaultFlag = page == 0 && limit == 0

        return calendarTermServices.getTermByCalId(calTermId, page, limit, defaultFlag)
                .let {
                    ResponseEntity.ok()
                            .header("Content-Type", Media.SIREN_TYPE)
                            .body(CalendarTermDetailRepr(it, page, limit))
                }

    }
}