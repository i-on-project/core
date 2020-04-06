package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version

const val iCalendarVersion = "2.0"

class Calendar(
    prod: ProductIdentifier,
    version: Version = Version(),
    components: MutableList<CalendarComponent>? = null
) : CalendarComponent(prod, version, subComponents = components) {
    constructor(
        prod: ProductIdentifier,
        version: Version = Version(),
        vararg components: CalendarComponent
    ) : this(prod, version, mutableListOf(*components))

    override val componentName: String
        get() = "VCALENDAR"

    override fun toString(): String {
        return super.toString()
    }
}