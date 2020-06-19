package org.ionproject.core.readApi.calendar.icalendar.properties

import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.PropertyParameter

interface ParameterizedProperty : Property {
    val parameters: List<PropertyParameter>
}