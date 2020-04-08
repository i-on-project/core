package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.calendar.CalendarScale
import org.ionproject.core.calendar.icalendar.properties.calendar.Method
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version

const val iCalendarVersion = "2.0"

class Calendar (
    val prod: ProductIdentifier,
    val version: Version = Version(),
    val scale: CalendarScale? = null,
    val method: Method? = null,
    val components: MutableList<CalendarComponent> = mutableListOf()
) : Iterable<CalendarComponent> {

    constructor(
        prod: ProductIdentifier,
        version: Version = Version(),
        scale: CalendarScale? = null,
        method: Method? = null,
        vararg components: CalendarComponent
    ) : this(prod, version, scale, method, mutableListOf(*components))

    override fun iterator(): Iterator<CalendarComponent> = components.iterator()
}