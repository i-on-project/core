package org.ionproject.core.readApi.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Integer

class PercentComplete(
    value: Int
) : Property {

    override val value: Integer = Integer(value)

    init {
        if (value < 0 || value > 100) throw IllegalArgumentException("Percentage values must be between 0 and 100.")
    }

    override val name: String
        get() = "PERCENT-COMPLETE"
}