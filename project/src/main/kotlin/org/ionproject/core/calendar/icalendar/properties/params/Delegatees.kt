package org.ionproject.core.calendar.icalendar.properties.params

import org.ionproject.core.calendar.icalendar.types.CalAddress

class Delegatees(vararg users: CalAddress) : PropertyParameter("DELEGATED_TO", *users)