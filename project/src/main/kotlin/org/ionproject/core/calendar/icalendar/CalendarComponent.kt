package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.Property

abstract class CalendarComponent(
    val properties: List<Property>,
    val components: MutableList<CalendarComponent>? = null
) {
    abstract val componentName: String

    override fun toString(): String {
        val builder = StringBuilder()
        builder.appendln("BEGIN:V$componentName")
        properties.forEach {
            builder.appendln(it.toString().iCalendarFold())
        }
        components?.forEach {
            builder.appendln(it)
        }
        builder.append("END:V$componentName")
        return builder.toString()
    }
}

private const val ICALENDAR_MAX_CONTENT_LINE_LENGTH = 75

private fun String.iCalendarFold(): String =
    this.chunkedSequence(ICALENDAR_MAX_CONTENT_LINE_LENGTH).reduce { acc, s ->
        return "$acc\r\n $s"
    }
