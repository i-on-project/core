package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.iCalendarFold
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.twoPhaseReduce

abstract class CalendarComponent(
    val uid: UniqueIdentifier,
    val dtStamp: DateTimeStamp,
    vararg properties: Property?
) {
    val properties = listOfNotNull(uid, dtStamp, *properties)

    abstract val componentName: String

    override fun toString(): String {
        val properties = this.properties.twoPhaseReduce({ it.toiCalendar().iCalendarFold() },{ acc, it ->
            "$acc$it"
        })

        return "BEGIN:$componentName\r\n${properties}END:$componentName\r\n"
    }

}
