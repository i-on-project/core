package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.ProdId
import org.ionproject.core.calendar.icalendar.properties.Version

const val iCalendarVersion = "2.0"

class Calendar(
    prod: ProdId,
    components: MutableList<CalendarComponent> = mutableListOf(),
    version: Version = Version()
) : CalendarComponent(
    listOf(
        prod, version
    ),
    components
) {
    override val componentName: String = "CALENDAR"
}