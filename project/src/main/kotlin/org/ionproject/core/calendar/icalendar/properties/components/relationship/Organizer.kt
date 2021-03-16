package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.CommonName
import org.ionproject.core.calendar.icalendar.properties.parameters.DirectoryEntryReference
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.properties.parameters.SentBy
import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

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
