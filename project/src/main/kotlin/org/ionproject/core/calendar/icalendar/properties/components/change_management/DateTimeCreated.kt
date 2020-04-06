package org.ionproject.core.calendar.icalendar.properties.components.change_management

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.DateTime

class DateTimeCreated(
    value: DateTime
) : Property(value) {
    override val name: String
        get() = "CREATED"
}