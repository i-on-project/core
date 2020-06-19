package org.ionproject.core.readApi.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.FreeText

class GeographicPosition(
    f1: Float,
    f2: Float
) : Property {

    override val value: FreeText = FreeText("$f1;$f2")

    override val name: String
        get() = "GEO"
}