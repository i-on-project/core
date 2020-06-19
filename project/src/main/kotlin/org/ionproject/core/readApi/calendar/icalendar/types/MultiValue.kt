package org.ionproject.core.readApi.calendar.icalendar.types

class MultiValue<out T>(
    vararg val values: T
) : ICalendarDataType, Iterable<ICalendarDataType>
        where T : ICalendarDataType {

    init {
        if (values.isEmpty()) throw IllegalArgumentException("Properties cannot be value-less.")
    }

    override val value: Any
        get() = values.map { it.value }

    override val name: String
        get() = values[0].name

    override fun iterator(): Iterator<ICalendarDataType> = values.iterator()
}