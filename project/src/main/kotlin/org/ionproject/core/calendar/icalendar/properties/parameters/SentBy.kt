package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class SentBy(user: CalendarUserAddress) : PropertyParameter {
  override val name: String = "SENT-BY"
  override val values: List<Any> = listOf(user)
}