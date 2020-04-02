package org.ionproject.core.calendar.icalendar.properties.params

import org.ionproject.core.calendar.icalendar.types.CalAddress

class GroupListMembership(vararg addresses: CalAddress) : PropertyParameter("MEMBER", *addresses)