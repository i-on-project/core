package org.ionproject.core.readApi.calendar.icalendar.properties

import org.ionproject.core.readApi.calendar.icalendar.types.ICalendarDataType

interface Property {
    val name: String
    val value: ICalendarDataType
}

