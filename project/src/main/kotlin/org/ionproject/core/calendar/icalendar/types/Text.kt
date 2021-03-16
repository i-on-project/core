package org.ionproject.core.calendar.icalendar.types

class Text(
    override val value: String
) : ICalendarDataType {

    override fun toString(): String = value

    override val name: String
        get() = "TEXT"

    override fun equals(other: Any?): Boolean {
        if (other is String) {
            return value == other
        }
        if (other is Text) {
            return value == other.value
        }
        return false
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
