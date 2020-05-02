package org.ionproject.core.calendar.icalendar.types

class PeriodOfTime private constructor(
    private val start: DateTime,
    private val end: DateTime?,
    private val duration: Duration?
) : ICalendarDataType {

    constructor(start: DateTime, end: DateTime) : this(start, end, null)
    constructor(start: DateTime, duration: Duration) : this(start, null, duration)

    companion object {
        private const val name = "PERIOD"
    }

    override val name: String
        get() = Companion.name

    override fun toString(): String = "$start/${end ?: duration}"
}