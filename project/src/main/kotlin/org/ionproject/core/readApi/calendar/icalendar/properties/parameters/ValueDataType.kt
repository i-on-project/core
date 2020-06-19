package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.readApi.calendar.icalendar.types.ICalendarDataType

class ValueDataType(type: ICalendarDataType) : PropertyParameter {
    override val name: String = "VALUE"
    override val values: List<Any> = listOf(type.name)
}