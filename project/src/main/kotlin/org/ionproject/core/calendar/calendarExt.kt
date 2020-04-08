package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text
import java.io.Writer

fun String.iCalendarFold(): String {
    val iCalendarMaxContentLineLength = 75
    val chunks = chunkedSequence(iCalendarMaxContentLineLength)
    return chunks.joinToString("\r\n ")
}


fun Array<out String>.toText(): Array<Text> = Array(size) { Text(get(it)) }
fun List<String>.toText() : List<Text> = map { Text(it) }
fun String.toText(): Text = Text(this)

fun Writer.writeln(obj: Any) {
    write(obj.toString() + "\r\n")
}
fun Writer.writeICalendar(string: String) {
    writeln(string.iCalendarFold())
}