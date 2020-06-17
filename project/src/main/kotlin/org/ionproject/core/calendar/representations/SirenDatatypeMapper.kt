package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.types.*

class SirenDatatypeMapper : DatatypeMapper() {
    override fun map(value: ICalendarDataType): Any {
        return when(value.javaClass) {
            Date::class.java -> map(value as Date)
            DateTime::class.java -> map(value as DateTime)
            Recur::class.java -> map(value as Recur)
            else -> value.value
        }
    }

    fun map(date: Date) : Any = "${date.year}-${date.month}-${date.day}"

    fun map(time: Time) : Any = "${time.hours}:${time.minutes}:${time.seconds}${if (time.utc) "Z" else ""}"

    fun map(dateTime: DateTime) : Any = "${map(dateTime.date)}T${map(dateTime.time)}"

    fun map(recur: Recur) : Any =
        buildString {
            append("FREQ=${recur.frequency}")

            recur.until?.let {
                val value = map(it)
                append(";UNTIL=${value}")
            }

            recur.count?.let {
                append(";COUNT=$it")
            }

            recur.interval?.let {
                append(";INTERVAL=$it")
            }

            recur.byDay?.let {
                append(";BYDAY=${it.joinToString(",")}")
            }
        }
}
