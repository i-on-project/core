package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.readApi.calendar.icalendar.types.CalendarUserAddress

class Delegatees(vararg users: CalendarUserAddress) : PropertyParameter {
    override val name: String = "DELEGATED_TO"
    override val values: List<Any> = users.toList()
}