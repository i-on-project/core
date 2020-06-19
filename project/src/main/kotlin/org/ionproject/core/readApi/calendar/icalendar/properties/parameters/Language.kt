package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

class Language(value: String) : PropertyParameter {
    override val name: String = "LANGUAGE"
    override val values: List<Any> = listOf(value)
}