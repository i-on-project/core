package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class UniqueIdentifier(
    value: String
) : Property(Text(value)) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "UID"
    }
}