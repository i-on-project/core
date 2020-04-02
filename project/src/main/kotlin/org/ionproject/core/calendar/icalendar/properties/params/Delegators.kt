package org.ionproject.core.calendar.icalendar.properties.params

import org.ionproject.core.calendar.icalendar.types.CalAddress

class Delegators(vararg users: CalAddress) : PropertyParameter("DELEGATED-FROM", *users)