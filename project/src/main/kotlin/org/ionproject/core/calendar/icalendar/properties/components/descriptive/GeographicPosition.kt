package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.FreeText

class GeographicPosition(
  f1: Float,
  f2: Float
) : Property {

  override val value: FreeText = FreeText("$f1;$f2")

  override val name: String
    get() = "GEO"
}