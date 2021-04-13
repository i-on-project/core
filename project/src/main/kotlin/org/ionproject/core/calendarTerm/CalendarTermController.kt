package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.representations.toCalendarTermDetailRepr
import org.ionproject.core.calendarTerm.representations.toCalendarTermListRepr
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarTermController(private val repo: CalendarTermRepoImpl) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_CALENDAR_TERMS, ResourceIds.VERSION)
    @GetMapping(Uri.calendarTerms)
    fun getTerms(pagination: Pagination): ResponseEntity<Siren> {
        val calTerms = repo.getTerms(pagination.page, pagination.limit)
        return ResponseEntity.ok(calTerms.toCalendarTermListRepr(pagination.page, pagination.limit))
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_CALENDAR_TERM, ResourceIds.VERSION)
    @GetMapping(Uri.calendarTermById)
    fun getCalendarTerm(
        @PathVariable calterm: String,
        pagination: Pagination
    ): ResponseEntity<Siren> {
        val calTerm = repo.getTermByCalId(calterm, pagination.page, pagination.limit)
        calTerm?.let { return ResponseEntity.ok(it.toCalendarTermDetailRepr(pagination.page, pagination.limit)) }
        return ResponseEntity.notFound().build()
    }
}
