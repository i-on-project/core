package org.ionproject.core.calendar.icalendar.properties.components.change_management

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.DateTime

class DateTimeCreated(
    override val value: DateTime
) : Property {
    override val name: String
        get() = "CREATED"
}
