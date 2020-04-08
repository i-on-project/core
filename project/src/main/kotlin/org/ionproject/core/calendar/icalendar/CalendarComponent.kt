package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.twoPhaseReduce

abstract class CalendarComponent(
    val uid: UniqueIdentifier,
    val dtStamp: DateTimeStamp,
    vararg properties: Property?
) : Iterable<Property> {
    val properties = listOfNotNull(uid, dtStamp, *properties)

    override fun iterator(): Iterator<Property> = properties.iterator()

    abstract val componentName: String
}
