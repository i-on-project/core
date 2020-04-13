package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.DateTime

class DateTimeCompleted(
    override val value: DateTime
) : Property {
    override val name: String
        get() = "COMPLETED"
}