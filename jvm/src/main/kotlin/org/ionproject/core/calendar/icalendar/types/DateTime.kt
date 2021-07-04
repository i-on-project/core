package org.ionproject.core.calendar.icalendar.types

class DateTime(
    val date: Date,
    val time: Time
) : ICalendarDataType {
    companion object {
        private const val iCalName = "DATE-TIME"
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
    }

    override val value: Any
        get() = toString()

    override val name: String
        get() = iCalName

    override fun toString(): String {
        return "${date}$SEPARATOR$time"
    }
}
