package org.ionproject.core.calendar.icalendar.properties.params

class CalendarUserType(type: Type = Type.INDIVIDUAL) : PropertyParameter("CUTYPE", type) {
    enum class Type {
        INDIVIDUAL,
        GROUP,
        RESOURCE,
        ROOM,
        UNKNOWN
    }
}