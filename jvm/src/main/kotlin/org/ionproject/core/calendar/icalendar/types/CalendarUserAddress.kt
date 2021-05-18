package org.ionproject.core.calendar.icalendar.types

class CalendarUserAddress(override val value: String) : ICalendarDataType {

    companion object {
        const val name: String = "CAL_ADDRESS"
    }

    override val name: String
        get() = Companion.name
}
