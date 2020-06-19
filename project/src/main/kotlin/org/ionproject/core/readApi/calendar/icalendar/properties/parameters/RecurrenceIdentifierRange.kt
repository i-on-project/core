package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

class RecurrenceIdentifierRange(value: Range = Range.THISANDFUTURE) : PropertyParameter {
    enum class Range {
        THISANDFUTURE
    }

    override val name: String = "RANGE"
    override val values: List<Any> = listOf(value)
}