package org.ionproject.core.calendar.icalendar.properties.parameters

class AlarmTriggerRelationship(value: Type) : PropertyParameter {

    enum class Type {
        START,
        END
    }

    override val name: String = "RELATED"

    override val values: List<Type> = listOf(value)
}