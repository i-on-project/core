package org.ionproject.core.calendar.icalendar.properties.calendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class Method(text: Text) : Property(text) {

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "METHOD"
    }
}