package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.readApi.calendar.icalendar.types.Uri

class AlternateTextRepresentation(uri: Uri) : PropertyParameter {
    override val name: String = "ALTREP"
    override val values: List<Any> = listOf(uri)
}