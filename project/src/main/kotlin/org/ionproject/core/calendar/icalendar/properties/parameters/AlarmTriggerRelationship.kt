package org.ionproject.core.calendar.icalendar.properties.parameters

class AlarmTriggerRelationship(value: Type) : PropertyParameter("RELATED", value) {
    enum class Type {
        START,
        END
    }
}