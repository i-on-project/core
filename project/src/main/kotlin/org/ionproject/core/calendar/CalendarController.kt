package org.ionproject.core.calendar

import org.ionproject.core.calendar.representations.calendarFromClassRepr
import org.ionproject.core.calendar.representations.calendarFromClassSectionRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.common.model.ClassSection
import org.ionproject.core.klass.Klass
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarController(private val repository: CalendarRepo) {

    @GetMapping(Uri.calendarByClass, produces = [Media.TEXT_CALENDAR, Media.SIREN_TYPE])
    fun getFromClass(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @RequestHeader("Accept") acceptHeader: Array<String> // TODO(use constant for Accept literal)
    ): ResponseEntity<Any> {
        val calendar = repository.getClassCalendar(cid, calterm)
        return ResponseEntity.ok(
            if (acceptHeader.contains(Media.TEXT_CALENDAR)) {
                calendar
            } else {
                calendarFromClassRepr(Klass(cid, null, calterm), calendar)
            }
        )
    }

    @GetMapping(Uri.calendarByClassSection, produces = [Media.TEXT_CALENDAR, Media.SIREN_TYPE])
    fun getFromClassSection(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @RequestHeader("Accept") acceptHeader: Array<String> // TODO(use constant for Accept literal)
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendar(cid, calterm, sid)

        return ResponseEntity.ok(
            if (acceptHeader.contains(Media.TEXT_CALENDAR)) {
                calendar
            } else {
                calendarFromClassSectionRepr(ClassSection(cid, null, calterm, sid, 0), calendar)
            }
        )
    }
}