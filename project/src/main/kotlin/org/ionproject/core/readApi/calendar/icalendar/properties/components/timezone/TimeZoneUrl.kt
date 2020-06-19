package org.ionproject.core.readApi.calendar.icalendar.properties.components.timezone

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Uri

class TimeZoneUrl(
    override val value: Uri
) : Property {
    override val name: String
        get() = "TZURL"
}