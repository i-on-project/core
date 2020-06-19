package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.readApi.calendar.icalendar.types.Uri

class DirectoryEntryReference(uri: Uri) : PropertyParameter {
    override val name: String = "DIR"
    override val values: List<Any> = listOf(uri)
}