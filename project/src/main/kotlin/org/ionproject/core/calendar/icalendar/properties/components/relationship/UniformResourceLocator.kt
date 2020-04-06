package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Uri

class UniformResourceLocator(
    value: Uri
) : Property(value) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "URL"
    }
}