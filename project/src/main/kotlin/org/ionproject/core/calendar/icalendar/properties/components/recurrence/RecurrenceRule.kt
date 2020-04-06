package org.ionproject.core.calendar.icalendar.properties.components.recurrence

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType

class RecurrenceRule : Property {
    override val name: String
        get() = "RRULE"
    override val value: ICalendarDataType
        get() = TODO("Not yet implemented")

}