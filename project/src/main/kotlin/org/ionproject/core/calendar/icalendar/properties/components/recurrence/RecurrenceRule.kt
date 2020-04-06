package org.ionproject.core.calendar.icalendar.properties.components.recurrence

import org.ionproject.core.calendar.icalendar.properties.Property

class RecurrenceRule : Property("") {
    override val name: String
        get() = "RRULE"

}