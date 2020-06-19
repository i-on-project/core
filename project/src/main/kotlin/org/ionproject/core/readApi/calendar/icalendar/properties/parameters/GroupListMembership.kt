package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.readApi.calendar.icalendar.types.CalendarUserAddress

class GroupListMembership(vararg addresses: CalendarUserAddress) : PropertyParameter {
    override val name: String = "MEMBER"
    override val values: List<Any> = addresses.toList()
}