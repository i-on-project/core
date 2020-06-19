package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

class TimeZoneIdentifier(tzid: String) : PropertyParameter {
    override val name: String = "TZID"
    override val values: List<Any> = listOf(tzid)
}