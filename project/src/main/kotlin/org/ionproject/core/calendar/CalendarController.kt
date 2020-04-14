package org.ionproject.core.calendar

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CalendarController(private val repository: CalendarRepository) {

    @GetMapping(Uri.calendarByClass, produces = [Media.TEXT_CALENDAR])
    fun getFromClass(@PathVariable cid: Int, @PathVariable calterm: String): ResponseEntity<Any> {
        val calendar = repository.getClassCalendar(cid, calterm)
        return if (calendar != null) ResponseEntity.ok(calendar)
        else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping(Uri.calendarByClassSection, produces = [Media.TEXT_CALENDAR])
    fun getFromClassSection(
        @PathVariable("classSection_id") sectionId: Int,
        @PathVariable("class_id") classId: Int,
        @PathVariable("course_id") courseId: Int
    ): ResponseEntity<Any> {
        val calendar = repository.getClassSectionCalendar(courseId, classId, sectionId)
        return if (calendar != null) ResponseEntity.ok(calendar)
        else {
            ResponseEntity.notFound().build()
        }
    }
}