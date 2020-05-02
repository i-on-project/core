package org.ionproject.core.calendar.icalendar.types

class Integer(
    override val value: Int
) : ICalendarDataType {
    operator fun compareTo(i: Int): Int = value.compareTo(i)

    override val name: String
        get() = "INTEGER"

}