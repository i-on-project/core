package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.AlternateTextRepresentation
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Summary(
    value: String,
    val alternateTextRepresentation: AlternateTextRepresentation? = null,
    val language: Language? = null
) : ParameterizedProperty {
    override val parameters: List<PropertyParameter>
        get() = listOfNotNull(alternateTextRepresentation, language)

    override val name: String
        get() = "SUMMARY"

    override val value: Text = value.toText()
}