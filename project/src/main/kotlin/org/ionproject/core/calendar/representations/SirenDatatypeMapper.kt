package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.Time

class SirenDatatypeMapper : DatatypeMapper() {
    override fun map(value: ICalendarDataType): Any {
        return when(value.javaClass) {
            Date::class.java -> map(value as Date)
            DateTime::class.java -> map(value as DateTime)
            else -> value.value
        }
    }

    fun map(date: Date) : Any = "${date.year}-${date.month}-${date.day}"
    fun map(time: Time) : Any = "${time.hours}:${time.minutes}:${time.seconds}${if (time.utc) "Z" else ""}"
    fun map(dateTime: DateTime) : Any = "${map(dateTime.date)}T${map(dateTime.time)}"
}
