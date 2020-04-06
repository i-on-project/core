package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class SentBy(user: CalendarUserAddress) : PropertyParameter("SENT-BY", user)