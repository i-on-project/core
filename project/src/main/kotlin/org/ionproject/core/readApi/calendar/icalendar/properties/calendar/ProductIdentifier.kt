package org.ionproject.core.readApi.calendar.icalendar.properties.calendar

import org.ionproject.core.readApi.calendar.icalendar.properties.Property
import org.ionproject.core.readApi.calendar.icalendar.types.Text
import org.ionproject.core.readApi.calendar.toText

class ProductIdentifier(
    text: String
) : Property {

    override val value: Text = text.toText()

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "PRODID"
    }
}