package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.AlternateTextRepresentation
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Contact(
    value: String,
    val alternateTextRepresentation: AlternateTextRepresentation? = null,
    val language: Language? = null
) : ParameterizedProperty {

    override val value: Text = value.toText()

    override val parameters: List<PropertyParameter> = listOfNotNull(alternateTextRepresentation, language)

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "CONTACT"
    }
}