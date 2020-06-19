package org.ionproject.core.readApi.calendar.icalendar.types

class Uri(
    override val value: String
) : ICalendarDataType {
    override val name: String
        get() = "URI"

    override fun toString(): String = value
}
