package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

class CalendarUserType(type: Type = Type.INDIVIDUAL) : PropertyParameter {

    enum class Type {
        INDIVIDUAL,
        GROUP,
        RESOURCE,
        ROOM,
        UNKNOWN
    }

    override val name: String = "CUTYPE"
    override val values: List<Any> = listOf(type)
}