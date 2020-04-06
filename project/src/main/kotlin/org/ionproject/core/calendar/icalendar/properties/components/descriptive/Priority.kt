package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property

class Priority(
    value: Int = 0
) : Property(value) {
    init {
        if (value < 0 || value > 9) throw IllegalArgumentException("Priority values must be between 0 and 9")
    }

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "PRIORITY"
    }
}