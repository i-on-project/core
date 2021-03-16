package org.ionproject.core.calendar.icalendar.types

import java.util.Base64

class Binary(override val value: String) : ICalendarDataType {

    override fun toString(): String = Base64.getEncoder().encodeToString(value.toByteArray())

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "BINARY"
    }
}
