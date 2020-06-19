package org.ionproject.core.readApi.calendar.icalendar.properties.components.relationship

import org.ionproject.core.readApi.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.RecurrenceIdentifierRange
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.TimeZoneIdentifier
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.readApi.calendar.icalendar.types.Date
import org.ionproject.core.readApi.calendar.icalendar.types.DateTime
import org.ionproject.core.readApi.calendar.icalendar.types.ICalendarDataType

class RecurrenceId private constructor(
    override val value: ICalendarDataType,
    val valueDataType: ValueDataType? = null,
    val timeZoneIdentifier: TimeZoneIdentifier? = null,
    val recurrenceIdentifierRange: RecurrenceIdentifierRange? = null
) : ParameterizedProperty {

    constructor(
        dateTime: DateTime,
        timeZoneIdentifier: TimeZoneIdentifier?,
        recurrenceIdentifierRange: RecurrenceIdentifierRange?
    ) : this(
        dateTime,
        null,
        timeZoneIdentifier,
        recurrenceIdentifierRange
    )

    constructor(
        date: Date,
        timeZoneIdentifier: TimeZoneIdentifier?,
        recurrenceIdentifierRange: RecurrenceIdentifierRange?
    ) : this(
        date,
        ValueDataType(date),
        timeZoneIdentifier,
        recurrenceIdentifierRange
    )

    override val parameters: List<PropertyParameter> =
        listOfNotNull(valueDataType, timeZoneIdentifier, recurrenceIdentifierRange)

    override val name: String
        get() = "RECURRENCE-ID"
}