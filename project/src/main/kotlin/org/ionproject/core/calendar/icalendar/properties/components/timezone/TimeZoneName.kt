package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.types.Text

class TimeZoneName(
    value: Text,
    language: Language?
) : Property(value, language) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "TZNAME"
    }
}