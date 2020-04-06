package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.twoPhaseReduce

abstract class CalendarComponent(
    vararg properties: Property?,
    private val subComponents: MutableList<CalendarComponent>? = null
) {
    val properties = listOfNotNull(*properties)

    abstract val componentName: String

    override fun toString(): String {
        val properties = this.properties.twoPhaseReduce({ it.toString().iCalendarFold() },{ acc, it ->
            "$acc$it"
        })

        val subComponents = this.subComponents?.twoPhaseReduce({ it.toString() },{ acc, it ->
            "$acc$it"
        }) ?: ""

        return "BEGIN:$componentName\r\n$properties${subComponents}END:$componentName\r\n"
    }
}

private const val ICALENDAR_MAX_CONTENT_LINE_LENGTH = 75

private fun String.iCalendarFold(): String {
    val chunks = chunkedSequence(ICALENDAR_MAX_CONTENT_LINE_LENGTH)
    return chunks.joinToString("\r\n ")
}
