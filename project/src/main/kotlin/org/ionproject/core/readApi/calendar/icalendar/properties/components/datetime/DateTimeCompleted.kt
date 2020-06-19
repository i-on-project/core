package org.ionproject.core.readApi.calendar.icalendar.properties.components.datetime

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.DateTime

class DateTimeCompleted(
    override val value: DateTime
) : Property {
    override val name: String
        get() = "COMPLETED"
}