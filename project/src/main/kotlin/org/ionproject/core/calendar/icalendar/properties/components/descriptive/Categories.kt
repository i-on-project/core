package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.types.Text

class Categories(
    value: List<String>,
    language: Language? = null
) : Property(value.map { Text(it) }, language) {
    override val name: String
        get() = "CATEGORIES"
}