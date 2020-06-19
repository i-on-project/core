package org.ionproject.core.readApi.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Text
import org.ionproject.core.readApi.calendar.toText

class Classification private constructor(
    value: String = "PUBLIC"
) : Property {
    override val value: Text = value.toText()

    override val name: String
        get() = "CLASS"

    companion object {
        val PUBLIC = Classification()
        val PRIVATE = Classification("PRIVATE")
        val CONFIDENTIAL = Classification("CONFIDENTIAL")
    }
}