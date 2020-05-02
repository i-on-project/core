package org.ionproject.core.calendar.icalendar.properties.parameters

class RSVPExpectation(expected: Boolean = false) : PropertyParameter {
    override val name: String = "RSVP"
    override val values: List<Any> = listOf(expected)
}