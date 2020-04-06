package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.types.Text

fun String.iCalendarFold(): String {
    val iCalendarMaxContentLineLength = 75
    val chunks = chunkedSequence(iCalendarMaxContentLineLength)
    return chunks.joinToString("\r\n ")
}

fun List<String>.toText() : List<Text> = map { Text(it) }
fun String.toText(): Text = Text(this)