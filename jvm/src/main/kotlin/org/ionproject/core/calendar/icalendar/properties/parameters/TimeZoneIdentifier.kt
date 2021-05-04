package org.ionproject.core.calendar.icalendar.properties.parameters

class TimeZoneIdentifier(tzid: String) : PropertyParameter {
    override val name: String = "TZID"
    override val values: List<Any> = listOf(tzid)
}
