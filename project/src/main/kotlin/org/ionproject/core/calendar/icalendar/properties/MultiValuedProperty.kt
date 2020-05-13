package org.ionproject.core.calendar.icalendar.properties

import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.MultiValue

interface MultiValuedProperty<out T> : Property
  where T : ICalendarDataType {
  override val value: MultiValue<T>
}