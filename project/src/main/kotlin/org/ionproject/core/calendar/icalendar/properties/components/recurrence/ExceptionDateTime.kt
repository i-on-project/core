package org.ionproject.core.calendar.icalendar.properties.components.recurrence

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.TimeZoneIdentifier
import org.ionproject.core.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime

class ExceptionDateTime private constructor(
    value: List<Any>,
    valueDataType: ValueDataType?,
    timeZoneIdentifier: TimeZoneIdentifier?
) : Property(value, valueDataType, timeZoneIdentifier) {


    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "EXDATE"
    }
}