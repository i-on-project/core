package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.MultiValuedProperty
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Categories (
    categories: List<String>,
    val language: Language? = null
) : ParameterizedProperty, MultiValuedProperty {

    val categories: List<Text> = categories.toText()

    override val parameters: List<PropertyParameter?>
        get() = listOf(language)
    override val values: List<ICalendarDataType>
        get() = categories
    override val name: String
        get() = "CATEGORIES"
}