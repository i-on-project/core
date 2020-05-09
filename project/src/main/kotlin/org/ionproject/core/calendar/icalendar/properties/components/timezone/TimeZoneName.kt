package org.ionproject.core.calendar.icalendar.properties.components.timezone

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class TimeZoneName(
    value: String,
    val language: Language? = null
) : ParameterizedProperty {
    override val parameters: List<PropertyParameter> = listOfNotNull(language)

    override val name: String
        get() = "TZNAME"

    override val value: Text = value.toText()
}