package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property

class PercentComplete(
    override val value: Integer
) : Property {

    init {
        if (value < 0 || value > 100) throw IllegalArgumentException("Percentage values must be between 0 and 100.")
    }

    override val name: String
        get() = "PERCENT-COMPLETE"
}