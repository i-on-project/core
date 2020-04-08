package org.ionproject.core.calendar.icalendar.properties.parameters

class CommonName(name: String): PropertyParameter {
    override val name: String = "CN"
    override val values: List<Any> = listOf(name.toPropertyParameterText())
}