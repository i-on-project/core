package org.ionproject.core.calendar.icalendar.properties.calendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Method(
  text: String
) : Property {

  override val value: Text = text.toText()

  override val name: String
    get() = iCalName

  companion object {
    private const val iCalName = "METHOD"
  }
}