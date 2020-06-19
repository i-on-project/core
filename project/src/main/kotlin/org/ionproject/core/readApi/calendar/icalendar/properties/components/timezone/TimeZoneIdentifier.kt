package org.ionproject.core.readApi.calendar.icalendar.properties.components.timezone

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Text
import org.ionproject.core.readApi.calendar.toText

class TimeZoneIdentifier(
    value: String
) : Property {

    override val value: Text = value.toText()

    override val name: String
        get() = "TZID"
}