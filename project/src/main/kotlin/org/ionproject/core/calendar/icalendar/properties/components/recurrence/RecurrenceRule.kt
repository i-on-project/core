package org.ionproject.core.calendar.icalendar.properties.components.recurrence

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Recur

class RecurrenceRule(
    override val value: Recur
) : Property {
    override val name: String
        get() = "RRULE"
}
