package org.ionproject.core.calendar.icalendar.properties.parameters

interface PropertyParameter {
    val name: String
    val values: List<Any>
}

fun String.toPropertyParameterText(): String =
    if ((contains(',') || contains(':') || contains(';')) && !startsAndEndsWith("\"")) {
        "\"${this}\""
    } else this

fun String.startsAndEndsWith(str: String): Boolean = startsWith(str) && endsWith(str)