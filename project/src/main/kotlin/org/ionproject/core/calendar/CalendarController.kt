package org.ionproject.core.calendar

import org.ionproject.core.ProblemJson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v0/**/calendar", produces = ["text/calendar", "application/vdn.siren+json"])
class CalendarController {
    private val service = CalendarService()

    @GetMapping("**/class/{id}/calendar", produces = [ "text/calendar" ])
    fun getFromClass(@PathVariable("id") id: Int, servlet: HttpServletRequest): ResponseEntity<Any> {
        val calendar = service.getClassCalendar(id)
        return if (calendar != null) ResponseEntity.ok(calendar.toString())
        else {
            val path = servlet.requestURI
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "application/problem+json")
                .body(ProblemJson(
                    "https://pt.wikipedia.org/wiki/HTTP_404",
                    "Non existent class",
                    404,
                    "The class whose calendar was trying to be obtained does not seem to exist. Try finding a valid class in ${path.removeSuffix("$id/calendar")}",
                    path
                )
            )
        }
    }
}