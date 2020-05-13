package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.properties.parameters.TimeZoneIdentifier
import org.ionproject.core.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType

class DateTimeEnd private constructor(
  override val value: ICalendarDataType,
  val valueDataType: ValueDataType?,
  val timeZoneIdentifier: TimeZoneIdentifier?
) : ParameterizedProperty {

  constructor(dateTime: DateTime, timeZoneIdentifier: TimeZoneIdentifier? = null) : this(
    dateTime,
    null,
    timeZoneIdentifier
  )

  constructor(date: Date, timeZoneIdentifier: TimeZoneIdentifier? = null) : this(
    date,
    ValueDataType(date),
    timeZoneIdentifier
  )

  override val parameters: List<PropertyParameter>
    get() = listOfNotNull(valueDataType, timeZoneIdentifier)

  override val name: String
    get() = "DTEND"
}