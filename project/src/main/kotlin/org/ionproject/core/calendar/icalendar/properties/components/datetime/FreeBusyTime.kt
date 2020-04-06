package org.ionproject.core.calendar.icalendar.properties.components.datetime

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.FreeBusyTimeType
import org.ionproject.core.calendar.icalendar.types.PeriodOfTime

class FreeBusyTime(
    value: PeriodOfTime,
    freeBusyTimeType: FreeBusyTimeType?
) : Property(value, freeBusyTimeType) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "FREEBUSY"
    }
}