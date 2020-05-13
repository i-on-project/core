package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Duration as DurationDataType

class Duration(
  override val value: DurationDataType
) : Property {
  init {
    if (!value.adding) throw IllegalArgumentException("Duration value for DURATION property must be positive.")
  }

  override val name: String
    get() = "DURATION"
}