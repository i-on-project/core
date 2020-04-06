package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property

class Priority(
    override val value: Int = 0
) : Property {
    init {
        if (value < 0 || value > 9) throw IllegalArgumentException("Priority values must be between 0 and 9")
    }

    override val name: String
        get() = "PRIORITY"
}