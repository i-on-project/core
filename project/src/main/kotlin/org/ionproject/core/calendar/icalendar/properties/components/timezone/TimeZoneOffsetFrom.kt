package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.UTCOffset

class TimeZoneOffsetFrom(
    value: UTCOffset
) : Property(value) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "TZOFFSETFROM"
    }
}