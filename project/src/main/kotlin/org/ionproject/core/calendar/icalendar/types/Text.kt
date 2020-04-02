package org.ionproject.core.calendar.icalendar.types

class Text(
    text: String
) {
    private val value: String

    init {
        var temp = text.replace(",", "\\,")
        temp = temp.replace(";", "\\;")
        value = temp
    }

    override fun toString(): String = value

}