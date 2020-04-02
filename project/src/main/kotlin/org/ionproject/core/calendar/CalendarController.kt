package org.ionproject.core.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calendar", produces = [ "text/plain" ] )
class CalendarController {
    private val repo = CalendarRepository()

    @GetMapping("/{id}")
    fun getFromClass(@PathVariable("id") id: Int) : String {
        val calendar = repo[id]
        println("GETTING FROM CLASS $id")
        val calendarString = calendar.toString()
        println(calendarString)
        return calendarString
    }
}