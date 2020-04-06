package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.TimeZoneIdentifier
import org.ionproject.core.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime

class DateTimeStart private constructor(
    value: Any,
    valueDataType: ValueDataType?,
    timeZoneIdentifier: TimeZoneIdentifier?
) : Property(value, valueDataType, timeZoneIdentifier) {

    constructor(dateTime: DateTime, timeZoneIdentifier: TimeZoneIdentifier? = null) : this(dateTime, null, timeZoneIdentifier)
    constructor(date: Date, timeZoneIdentifier: TimeZoneIdentifier? = null) : this(date, ValueDataType(date), timeZoneIdentifier)

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "DTSTART"

    }
}