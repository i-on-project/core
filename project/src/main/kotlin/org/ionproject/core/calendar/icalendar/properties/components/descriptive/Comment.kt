package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.AlternateTextRepresentation
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.types.Text

class Comment(
    value: Text,
    alternateTextRepresentation: AlternateTextRepresentation?,
    language: Language?
) : Property(value, alternateTextRepresentation, language) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "COMMENT"
    }
}