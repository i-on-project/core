package org.ionproject.core.calendar.icalendar.properties

import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter

interface ParameterizedProperty : Property {
    val parameters: List<PropertyParameter>
}