package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property

class GeographicPosition(
    f1: Float,
    f2: Float
) : Property("$f1;$f2") {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "GEO"
    }
}