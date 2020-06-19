package org.ionproject.core.readApi.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Integer

class Priority(
    value: Int = 0
) : Property {
    override val value: Integer = Integer(value)

    init {
        if (value < 0 || value > 9) throw IllegalArgumentException("Priority values must be between 0 and 9")
    }

    override val name: String
        get() = "PRIORITY"
}