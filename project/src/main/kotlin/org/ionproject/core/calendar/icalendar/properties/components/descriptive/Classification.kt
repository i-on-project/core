package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Classification private constructor(
    value: String = "PUBLIC"
) : Property {
    override val value : Text = value.toText()

    override val name: String
        get() = "CLASS"

    companion object {
        val PUBLIC = Classification()
        val PRIVATE = Classification("PRIVATE")
        val CONFIDENTIAL = Classification("CONFIDENTIAL")
    }
}