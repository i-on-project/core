package org.ionproject.core.readApi.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.readApi.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.AlternateTextRepresentation
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.readApi.calendar.toText

class Comment(
    value: String,
    val alternateTextRepresentation: AlternateTextRepresentation? = null,
    val language: Language? = null
) : ParameterizedProperty {

    override val value = value.toText()

    override val parameters: List<PropertyParameter>
        get() = listOfNotNull(alternateTextRepresentation, language)
    override val name: String
        get() = "COMMENT"
}