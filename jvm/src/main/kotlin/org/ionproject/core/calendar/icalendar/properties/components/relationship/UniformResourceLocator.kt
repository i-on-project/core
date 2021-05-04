package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Uri

class UniformResourceLocator(
    override val value: Uri
) : Property {
    override val name: String
        get() = "URL"
}
