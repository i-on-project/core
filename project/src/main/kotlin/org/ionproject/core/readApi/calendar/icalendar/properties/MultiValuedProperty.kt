package org.ionproject.core.readApi.calendar.icalendar.properties

import org.ionproject.core.readApi.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.readApi.calendar.icalendar.types.MultiValue

interface MultiValuedProperty<out T> : Property
        where T : ICalendarDataType {
    override val value: MultiValue<T>
}