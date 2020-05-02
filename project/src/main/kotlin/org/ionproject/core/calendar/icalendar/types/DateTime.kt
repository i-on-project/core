package org.ionproject.core.calendar.icalendar.types

class DateTime(
    private val date: Date,
    private val time: Time
) : ICalendarDataType {
    override val value: Any
        get() = toString()

    override val name: String
        get() = iCalName

    companion object {
        private const val SEPARATOR = 'T'

        fun parse(s: CharSequence): DateTime {
            val tIndex = s.indexOf(SEPARATOR)
            val dateComp = s.subSequence(0, tIndex)
            val timeComp = s.subSequence(tIndex + 1, s.length)
            return DateTime(
                Date.parse(dateComp),
                Time.parse(timeComp)
            )
        }

        private const val iCalName = "DATE-TIME"
    }

    override fun toString(): String {
        return "${date}$SEPARATOR$time"
    }
}