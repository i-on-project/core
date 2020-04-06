package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class GroupListMembership(vararg addresses: CalendarUserAddress) : PropertyParameter("MEMBER", *addresses)