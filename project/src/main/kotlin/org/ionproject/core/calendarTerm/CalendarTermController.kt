package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.representations.toCalendarTermDetailRepr
import org.ionproject.core.calendarTerm.representations.toCalendarTermListRepr
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarTermController(private val repo: CalendarTermRepoImpl) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_CALENDAR_TERMS, ResourceIds.VERSION)
    @GetMapping(Uri.calendarTerms)
    fun getTerms(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Siren> {
        val calTerms = repo.getTerms(page, limit)

        return ResponseEntity.ok(calTerms.toCalendarTermListRepr(page, limit))
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_CALENDAR_TERM, ResourceIds.VERSION)
    @GetMapping(Uri.calendarTermById)
    fun getCalendarTerm(
        @PathVariable calterm: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Siren> {
        val calTerm = repo.getTermByCalId(calterm, page, limit)

        calTerm?.let { return ResponseEntity.ok(it.toCalendarTermDetailRepr(page, limit)) }
        return ResponseEntity.notFound().build()
    }
}
