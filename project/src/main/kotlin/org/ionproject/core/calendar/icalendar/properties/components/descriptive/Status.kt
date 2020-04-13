package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Status private constructor(
    value: String
) : Property {

    override val value: Text = value.toText()

    override val name: String
        get() = "STATUS"

    companion object {
        val TENTATIVE = Status("TENTATIVE")
        val CONFIRMED = Status("CONFIRMED")
        val CANCELLED = Status("CANCELLED")
        val NEEDS_ACTION = Status("NEEDS-ACTION")
        val COMPLETED = Status("COMPLETED")
        val IN_PROCESS = Status("IN-PROCESS")
        val DRAFT = Status("DRAFT")
        val FINAL = Status("FINAL")
    }
}