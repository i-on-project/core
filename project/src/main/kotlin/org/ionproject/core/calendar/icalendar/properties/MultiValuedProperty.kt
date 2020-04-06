package org.ionproject.core.calendar.icalendar.properties

import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.toText

interface MultiValuedProperty : Property {
    val values: List<ICalendarDataType>

    override val value: ICalendarDataType
        get() = values.joinToString(",").toText()
}