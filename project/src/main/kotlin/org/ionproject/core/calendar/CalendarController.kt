package org.ionproject.core.calendar

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarController(private val repository: CalendarRepo) {

    @GetMapping(Uri.calendarByClass, produces = [Media.CALENDAR])
    fun fromClass(
        @PathVariable cid: Int,
        @PathVariable calterm: String
    ): ResponseEntity<Any> {
        val calendar = repository.getClassCalendar(cid, calterm)
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.calendarByClassSection, produces = [Media.CALENDAR])
    fun fromClassSection(
        @PathVariable sid: Int,
        @PathVariable calterm: String,
        @PathVariable cid: Int
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendar(cid, calterm, sid)
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.componentByClassCalendar, produces = [Media.CALENDAR])
    fun fromClassCalendar(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @PathVariable cmpid: String
    ): ResponseEntity<Any> {
        val calendar = repository.getClassCalendarComponent(cid, calterm, cmpid.toInt(16)) // TODO(use constant for uid radix)
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.componentByClassSectionCalendar, produces = [Media.CALENDAR, Media.SIREN_TYPE])
    fun fromClassSectionCalendar(
        @PathVariable sid: Int,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @PathVariable cmpid: String
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendarComponent(cid, calterm, sid, cmpid.toInt(16)) // TODO(use constant for uid radix)
        return if (calendar != null) {
            ResponseEntity.ok(calendar)
        } else
            ResponseEntity.notFound().build()
    }
}