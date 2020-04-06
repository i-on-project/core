package org.ionproject.core.calendar.icalendar.properties.parameters

class RecurrenceIdentifierRange(value: Range = Range.THISANDFUTURE) : PropertyParameter("RANGE", value) {
    enum class Range {
        THISANDFUTURE
    }
}