package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.CommonName
import org.ionproject.core.calendar.icalendar.properties.parameters.DirectoryEntryReference
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.SentBy
import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class Organizer(
    value: CalendarUserAddress,
    language: Language?,
    commonName: CommonName?,
    directoryEntryReference: DirectoryEntryReference?,
    sentBy: SentBy?
) : Property(value, language, commonName, directoryEntryReference, sentBy) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "ORGANIZER"
    }
}