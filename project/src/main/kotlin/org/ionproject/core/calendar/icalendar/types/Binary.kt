package org.ionproject.core.calendar.icalendar.types

import java.util.*

class Binary(val value: String) : ICalendarDataType {

    override fun toString(): String = Base64.getEncoder().encodeToString(value.toByteArray())

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "BINARY"
    }
}