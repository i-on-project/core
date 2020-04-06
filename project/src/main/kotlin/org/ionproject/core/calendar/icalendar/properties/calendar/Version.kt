package org.ionproject.core.calendar.icalendar.properties.calendar

import org.ionproject.core.calendar.icalendar.iCalendarVersion
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class Version(text: Text = Text(iCalendarVersion)) : Property(text) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "VERSION"
    }
}