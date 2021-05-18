package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.UTCOffset

class TimeZoneOffsetTo(
    override val value: UTCOffset
) : Property {
    override val name: String
        get() = "TZOFFSETTO"
}
