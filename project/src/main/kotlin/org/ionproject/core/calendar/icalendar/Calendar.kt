package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.calendar.CalendarScale
import org.ionproject.core.calendar.icalendar.properties.calendar.Method
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version

const val iCalendarVersion = "2.0"

class Calendar(
    prod: ProductIdentifier,
    version: Version = Version(),
    scale: CalendarScale? = null,
    method: Method? = null,
    components: MutableList<CalendarComponent>? = null
) : CalendarComponent(prod, version, subComponents = components) {
    constructor(
        prod: ProductIdentifier,
        version: Version = Version(),
        scale: CalendarScale? = null,
        method: Method? = null,
        vararg components: CalendarComponent
    ) : this(prod, version, scale, method, mutableListOf(*components))

    override val componentName: String
        get() = "VCALENDAR"

    override fun toString(): String {
        return super.toString()
    }
}