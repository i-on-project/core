package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class TimeZoneIdentifier(
  value: String
) : Property {

  override val value: Text = value.toText()

  override val name: String
    get() = "TZID"
}