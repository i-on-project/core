package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property

class GeographicPosition(
    f1: Float,
    f2: Float
) : Property {

    override val value: String = "$f1;$f2"

    override val name: String
        get() = "GEO"
}