package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

import org.ionproject.core.startsAndEndsWith

interface PropertyParameter {
    val name: String
    val values: List<Any>
}

fun String.toPropertyParameterText(): String =
    if ((contains(',') || contains(':') || contains(';')) && !startsAndEndsWith("\"")) {
        "\"${this}\""
    } else this