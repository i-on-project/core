package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.Uri

class DirectoryEntryReference(uri: Uri) : PropertyParameter {
  override val name: String = "DIR"
  override val values: List<Any> = listOf(uri)
}