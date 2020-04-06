package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.DateTime

class DateTimeCompleted(
    value: DateTime
) : Property(value) {


    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "COMPLETED"
    }
}