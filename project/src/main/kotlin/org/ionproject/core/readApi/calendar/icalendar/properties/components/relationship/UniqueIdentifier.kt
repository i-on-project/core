package org.ionproject.core.readApi.calendar.icalendar.properties.components.relationship

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.readApi.calendar.toText

class UniqueIdentifier(
    value: String
) : Property {

    override val value: ICalendarDataType = value.toText()

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "UID"
    }
}