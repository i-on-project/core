package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.readApi.calendar.icalendar.types.CalendarUserAddress

class Delegators(vararg users: CalendarUserAddress) : PropertyParameter {
    override val name: String = "DELEGATED-FROM"
    override val values: List<Any> = users.toList()
}