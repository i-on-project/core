package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.representations.toCalendarTermDetailRepr
import org.ionproject.core.calendarTerm.representations.toCalendarTermListRepr
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

    @GetMapping(Uri.calendarTerms, produces = [Media.SIREN_TYPE])
    fun getTerms(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Siren> {
        val calTerms = calendarTermServices.getTerms(page, limit)

        return ResponseEntity.ok(calTerms.toCalendarTermListRepr(page, limit))
    }

    @GetMapping(Uri.calendarTermById, produces = [Media.SIREN_TYPE])
    fun getCalendarTerm(
        @PathVariable calterm: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Siren> {
        val calTerm = calendarTermServices.getTermByCalId(calterm, page, limit)

        calTerm?.let { return ResponseEntity.ok(it.toCalendarTermDetailRepr(page, limit)) }
        return ResponseEntity.notFound().build()
    }
}