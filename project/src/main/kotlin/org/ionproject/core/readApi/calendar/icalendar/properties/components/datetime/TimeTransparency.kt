package org.ionproject.core.readApi.calendar.icalendar.properties.components.datetime

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Text
import org.ionproject.core.readApi.calendar.toText

class TimeTransparency private constructor(
    value: String
) : Property {
    override val name: String
        get() = "TRANSP"

    override val value: Text = value.toText()

    companion object {
        val OPAQUE = TimeTransparency("OPAQUE")
        val TRANSPARENT = TimeTransparency("TRANSPARENT")
    }
}