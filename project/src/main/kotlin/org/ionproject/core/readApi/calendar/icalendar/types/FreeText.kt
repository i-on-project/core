package org.ionproject.core.readApi.calendar.icalendar.types

class FreeText(
    override val value: String
) : ICalendarDataType {

    /**
     * This is not a defined type in iCalendar. It is mentioned as a constraint free [Text] type.
     */
    override val name: String
        get() = ""
}