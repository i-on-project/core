package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.UTCOffset

class TimeZoneOffsetFrom(
    override val value: UTCOffset
) : Property {
    override val name: String
        get() = "TZOFFSETFROM"
}