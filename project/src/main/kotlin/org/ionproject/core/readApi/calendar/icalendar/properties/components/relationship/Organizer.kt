package org.ionproject.core.readApi.calendar.icalendar.properties.components.relationship

import org.ionproject.core.readApi.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.*
import org.ionproject.core.readApi.calendar.icalendar.types.CalendarUserAddress

class Organizer(
    override val value: CalendarUserAddress,
    val language: Language? = null,
    val commonName: CommonName? = null,
    val directoryEntryReference: DirectoryEntryReference? = null,
    val sentBy: SentBy? = null
) : ParameterizedProperty {

    override val parameters: List<PropertyParameter> =
        listOfNotNull(language, commonName, directoryEntryReference, sentBy)

    override val name: String
        get() = "ORGANIZER"
}