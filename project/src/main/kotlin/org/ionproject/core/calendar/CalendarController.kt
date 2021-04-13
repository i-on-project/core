package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.representations.toSiren
import org.ionproject.core.common.Media
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Uri
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.hexStringToInt
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CalendarController(private val repository: CalendarRepo) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_CALENDAR_CLASS, ResourceIds.VERSION_0)
    @GetMapping(Uri.calendarByClass)
    fun fromClass(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @RequestParam query: MultiValueMap<String, String>
    ): ResponseEntity<Calendar> {
        val calendar =
            repository.getClassCalendar(cid, calterm, query)
        return if (calendar != null)
            ResponseEntity.ok(calendar)
        else
            ResponseEntity.notFound().build()
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_CALENDAR_CLASS_SECTION, ResourceIds.VERSION_0)
    @GetMapping(Uri.calendarByClassSection)
    fun fromClassSection(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @RequestParam query: MultiValueMap<String, String>
    ): ResponseEntity<Calendar> {
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

    @ResourceIdentifierAnnotation(ResourceIds.GET_COMPONENT_CLASS, ResourceIds.VERSION_0)
    @GetMapping(Uri.componentByClassCalendar)
    fun fromClassCalendar(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @PathVariable cmpid: String,
        @RequestHeader("accept", required = false) acceptHeader: Array<String>?
    ): ResponseEntity<Any> {
        val calendar =
            repository.getClassCalendarComponent(cid, calterm, cmpid.hexStringToInt())
        return if (calendar != null) {

            val component: Any =
                formatComponent(
                    calendar,
                    acceptHeader,
                    Uri.forKlassByCalTerm(cid, calterm)
                ) ?: return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()

            ResponseEntity.ok(component)
        } else
            throw ResourceNotFoundException("This component doesn't exist.")
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_COMPONENT_CLASS_SECTION, ResourceIds.VERSION_0)
    @GetMapping(Uri.componentByClassSectionCalendar)
    fun fromClassSectionCalendar(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @PathVariable cmpid: String,
        @RequestHeader("accept", required = false) acceptHeader: Array<String>?
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendarComponent(
            cid,
            calterm,
            sid,
            cmpid.hexStringToInt()
        )
        return if (calendar != null) {

            val component: Any =
                formatComponent(
                    calendar,
                    acceptHeader,
                    Uri.forClassSectionById(cid, calterm, sid)
                ) ?: return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()

            ResponseEntity.ok(component)
        } else
            throw ResourceNotFoundException("This component doesn't exist.")
    }

    /**
     * This can return [Calendar] or [CalendarComponent] depending on Accept-header.
     * If the Accept header has something that isn't application/vnd.siren+json or text/calendar
     * the response will be null
     */
    private fun formatComponent(calendar: Calendar, acceptHeader: Array<String>?, path: URI): Any? =
        if (acceptHeader.isNullOrEmpty())
            calendar
        else {
            acceptHeader.forEach {
                when (it) {
                    Media.SIREN_TYPE -> {
                        return calendar.components[0].toSiren(path)
                    }
                    Media.CALENDAR -> {
                        return calendar
                    }
                    Media.ALL -> {
                        return calendar
                    }
                }
            }
            null
        }
}
