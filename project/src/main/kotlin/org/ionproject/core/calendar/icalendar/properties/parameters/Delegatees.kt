package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class Delegatees(vararg users: CalendarUserAddress) : PropertyParameter("DELEGATED_TO", *users)