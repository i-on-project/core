package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.RecurrenceIdentifierRange
import org.ionproject.core.calendar.icalendar.properties.parameters.TimeZoneIdentifier
import org.ionproject.core.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime

class RecurrenceId private constructor(
    value: Any,
    valueDataType: ValueDataType?,
    timeZoneIdentifier: TimeZoneIdentifier?,
    recurrenceIdentifierRange: RecurrenceIdentifierRange?
) : Property(value, valueDataType, timeZoneIdentifier, recurrenceIdentifierRange) {

    constructor(dateTime: DateTime, timeZoneIdentifier: TimeZoneIdentifier?, recurrenceIdentifierRange: RecurrenceIdentifierRange?) : this(
        dateTime,
        null,
        timeZoneIdentifier,
        recurrenceIdentifierRange
    )

    constructor(date: Date, timeZoneIdentifier: TimeZoneIdentifier?, recurrenceIdentifierRange: RecurrenceIdentifierRange?) : this(
        date,
        ValueDataType(date),
        timeZoneIdentifier,
        recurrenceIdentifierRange
    )

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "RECURRENCE-ID"
    }
}