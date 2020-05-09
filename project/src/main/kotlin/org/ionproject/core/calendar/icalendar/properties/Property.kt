package org.ionproject.core.calendar.icalendar.properties

import org.ionproject.core.calendar.icalendar.types.ICalendarDataType

interface Property {
    val name: String
    val value: ICalendarDataType
}

