package org.ionproject.core.calendar.icalendar.properties.calendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class CalendarScale(value: Text = Text("GREGORIAN")) : Property(value) {

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "CALSCALE"
    }
}