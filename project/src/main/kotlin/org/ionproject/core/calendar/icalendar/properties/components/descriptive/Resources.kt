package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.MultiValuedProperty
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.AlternateTextRepresentation
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Resources(
    value: List<String>,
    val alternateTextRepresentation: AlternateTextRepresentation? = null,
    val language: Language? = null
) : MultiValuedProperty, ParameterizedProperty {
    override val values: List<ICalendarDataType> = value.toText()

    override val parameters: List<PropertyParameter?>
        get() = listOf(alternateTextRepresentation, language)

    override val name: String
        get() = "RESOURCES"
}