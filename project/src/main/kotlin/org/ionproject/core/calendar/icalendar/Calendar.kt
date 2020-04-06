package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.iCalendarFold
import org.ionproject.core.calendar.icalendar.properties.calendar.CalendarScale
import org.ionproject.core.calendar.icalendar.properties.calendar.Method
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.twoPhaseReduce

const val iCalendarVersion = "2.0"

class Calendar (
    private val prod: ProductIdentifier,
    private val version: Version = Version(),
    private val scale: CalendarScale? = null,
    private val method: Method? = null,
    private val components: MutableList<CalendarComponent> = mutableListOf()
) : Iterable<CalendarComponent> {
    constructor(
        prod: ProductIdentifier,
        version: Version = Version(),
        scale: CalendarScale? = null,
        method: Method? = null,
        vararg components: CalendarComponent
    ) : this(prod, version, scale, method, mutableListOf(*components))

    override fun toString(): String {
        val properties = listOfNotNull(prod, version, scale, method).twoPhaseReduce({ it.toiCalendar().iCalendarFold() },{ acc, it ->
            "$acc$it"
        })

        val subComponents = components.twoPhaseReduce({ it.toString() },{ acc, it ->
            "$acc$it"
        })

        return "BEGIN:VCALENDAR\r\n${properties}${subComponents}END:VCALENDAR\r\n"
    }

    override fun iterator(): Iterator<CalendarComponent> = components.iterator()
}