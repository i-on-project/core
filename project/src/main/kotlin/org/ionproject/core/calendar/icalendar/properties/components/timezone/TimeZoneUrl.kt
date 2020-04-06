package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Uri

class TimeZoneUrl(
    override val value: Uri
) : Property {
    override val name: String
        get() = "TZURL"
}