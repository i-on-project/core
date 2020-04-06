package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class Delegators(vararg users: CalendarUserAddress) : PropertyParameter("DELEGATED-FROM", *users)