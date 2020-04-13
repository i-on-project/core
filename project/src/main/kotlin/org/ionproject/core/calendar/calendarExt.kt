package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.types.Text

fun String.iCalendarFold(): String {
    val iCalendarMaxContentLineLength = 75
    val chunks = chunkedSequence(iCalendarMaxContentLineLength)
    return chunks.joinToString("\r\n ")
}

fun Array<out String>.toText(): Array<Text> = Array(size) { Text(get(it)) }
fun List<String>.toText(): List<Text> = map { Text(it) }
fun String.toText(): Text = Text(this)

