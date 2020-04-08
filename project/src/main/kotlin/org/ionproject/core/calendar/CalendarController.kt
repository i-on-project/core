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
@RequestMapping()
class CalendarController {
    private val service = CalendarService()

    @GetMapping("/v0/course/{course_id}/class/{class_id}/calendar", produces = [ "text/calendar" ])
    fun getFromClass(@PathVariable("class_id") classId: String, @PathVariable("course_id") courseId: String): ResponseEntity<Any> {
        val calendar = service.getClassCalendar(classId)
        return if (calendar != null) ResponseEntity.ok(calendar)
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "application/problem+json")
                .body(ProblemJson(
                    "https://pt.wikipedia.org/wiki/HTTP_404",
                    "Non existent class",
                    404,
                    "The class whose calendar was trying to be obtained does not seem to exist. Try finding a valid class in /v0/course/$courseId/class",
                    "/v0/course/$courseId/class/$classId/calendar"
                )
            )
        }
    }
}