package org.ionproject.core.calendar

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarController(private val repository: CalendarRepo) {

    @GetMapping(Uri.calendarByClass, produces = [Media.TEXT_CALENDAR])
    fun getFromClass(@PathVariable cid: Int, @PathVariable calterm: String): ResponseEntity<Any> {
        val calendar = repository.getClassCalendar(cid, calterm)
        return ResponseEntity.ok(calendar)
    }

    @GetMapping(Uri.calendarByClassSection, produces = [Media.TEXT_CALENDAR])
    fun getFromClassSection(
        @PathVariable sid: Int,
        @PathVariable calterm: String,
        @PathVariable cid: Int
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendar(cid, calterm, sid)
        return ResponseEntity.ok(calendar)
    }
}