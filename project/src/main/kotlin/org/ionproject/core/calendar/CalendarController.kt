package org.ionproject.core.calendar

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.hexStringToInt
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController



@RestController
class CalendarController(private val repository: CalendarRepo) {

    @GetMapping(Uri.calendarByClass, produces = [Media.CALENDAR, Media.SIREN_TYPE])
    fun fromClass(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @RequestParam query: MultiValueMap<String, String>
    ): ResponseEntity<Any> {
        val calendar =
            repository.getClassCalendar(cid, calterm, query)
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.calendarByClassSection, produces = [Media.CALENDAR, Media.SIREN_TYPE])
    fun fromClassSection(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @RequestParam query: MultiValueMap<String, String>
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendar(
            cid,
            calterm,
            sid,
            query
        )
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.componentByClassCalendar, produces = [Media.CALENDAR, Media.SIREN_TYPE])
    fun fromClassCalendar(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @PathVariable cmpid: String
    ): ResponseEntity<Any> {
        val calendar =
            repository.getClassCalendarComponent(cid, calterm, cmpid.hexStringToInt())
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.componentByClassSectionCalendar, produces = [Media.CALENDAR, Media.SIREN_TYPE])
    fun fromClassSectionCalendar(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @PathVariable cmpid: String
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendarComponent(
            cid,
            calterm,
            sid,
            cmpid.hexStringToInt()
        )
        return if (calendar != null) {
            ResponseEntity.ok(calendar)
        } else
            ResponseEntity.notFound().build()
    }
}