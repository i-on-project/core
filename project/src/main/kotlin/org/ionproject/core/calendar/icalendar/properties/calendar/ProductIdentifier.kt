package org.ionproject.core.calendar.icalendar.properties.calendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class ProductIdentifier(text: String) : Property(Text(text)) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "PRODID"
    }
}