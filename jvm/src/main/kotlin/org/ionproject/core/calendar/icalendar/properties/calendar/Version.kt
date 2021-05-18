package org.ionproject.core.calendar.icalendar.properties.calendar

import org.ionproject.core.calendar.icalendar.iCalendarVersion
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Version(
    value: String = iCalendarVersion
) : Property {

    override val value: Text = value.toText()

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "VERSION"
    }
}
