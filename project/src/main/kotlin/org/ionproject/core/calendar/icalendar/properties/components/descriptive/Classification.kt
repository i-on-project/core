package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text

class Classification private constructor(
    value: String = "PUBLIC"
) : Property(Text(value)) {
    override val name: String
        get() = iCalName

    companion object {
        val PUBLIC = Classification()
        val PRIVATE = Classification("PRIVATE")
        val CONFIDENTIAL = Classification("CONFIDENTIAL")
        private const val iCalName = "CLASS"
    }
}