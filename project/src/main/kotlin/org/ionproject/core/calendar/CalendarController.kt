package org.ionproject.core.calendar

import org.ionproject.core.ProblemJson
import org.ionproject.core.common.PROBLEM_JSON
import org.ionproject.core.common.TEXT_CALENDAR
import org.ionproject.core.common.Uri
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping()
class CalendarController {
    private val service = CalendarService()

    @GetMapping(Uri.calendarByClass, produces = [ TEXT_CALENDAR ])
    fun getFromClass(@PathVariable("calterm") calendarTerm: String, @PathVariable("acr") courseAcronym: String): ResponseEntity<Any> {
        val calendar = service.getClassCalendar(calendarTerm)
        return if (calendar != null) ResponseEntity.ok(calendar)
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", PROBLEM_JSON)
                .body(ProblemJson(
                    "https://pt.wikipedia.org/wiki/HTTP_404",
                    "Non existent class",
                    404,
                    "The class whose calendar was trying to be obtained does not seem to exist. Try finding a valid class in /v0/course/$courseAcronym/class",
                    "/v0/course/$courseAcronym/class/$calendarTerm/calendar"
                )
            )
        }
    }
}