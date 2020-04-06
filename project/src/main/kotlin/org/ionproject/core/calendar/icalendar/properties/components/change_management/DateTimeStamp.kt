package org.ionproject.core.calendar.icalendar.properties.components.change_management

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.DateTime

class DateTimeStamp(
    value: DateTime
) : Property(value) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "DTSTAMP"
    }
}