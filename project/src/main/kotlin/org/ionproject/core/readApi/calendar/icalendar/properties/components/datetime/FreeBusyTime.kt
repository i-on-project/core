package org.ionproject.core.readApi.calendar.icalendar.properties.components.datetime

import org.ionproject.core.readApi.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.FreeBusyTimeType
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.readApi.calendar.icalendar.types.PeriodOfTime

class FreeBusyTime(
    override val value: PeriodOfTime,
    val freeBusyTimeType: FreeBusyTimeType? = null
) : ParameterizedProperty {
    override val parameters: List<PropertyParameter>
        get() = listOfNotNull(freeBusyTimeType)
    override val name: String
        get() = "FREEBUSY"
}