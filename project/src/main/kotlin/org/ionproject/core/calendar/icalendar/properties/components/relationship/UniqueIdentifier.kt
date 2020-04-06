package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class UniqueIdentifier private constructor(
    value: String
) : Property {

    override val value: ICalendarDataType = value.toText()

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "UID"
    }
}