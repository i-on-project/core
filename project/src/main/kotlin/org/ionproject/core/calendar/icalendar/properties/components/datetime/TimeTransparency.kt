package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class TimeTransparency private constructor(
    value: String
) : Property(Text(value)) {
    override val name: String
        get() = iCalName

    companion object {
        val OPAQUE = TimeTransparency("OPAQUE")
        val TRANSPARENT = TimeTransparency("TRANSPARENT")

        private const val iCalName = "TRANSP"
    }
}