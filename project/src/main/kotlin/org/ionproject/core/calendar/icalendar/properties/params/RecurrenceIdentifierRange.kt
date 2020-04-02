package org.ionproject.core.calendar.icalendar.properties.params

class RecurrenceIdentifierRange(value: Range = Range.THISANDFUTURE) : PropertyParameter("RANGE", value) {
    enum class Range {
        THISANDFUTURE
    }
}