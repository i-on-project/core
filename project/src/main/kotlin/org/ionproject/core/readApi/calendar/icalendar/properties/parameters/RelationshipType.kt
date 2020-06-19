package org.ionproject.core.readApi.calendar.icalendar.properties.parameters

class RelationshipType(relation: Type = Type.PARENT) : PropertyParameter {
    enum class Type {
        PARENT,
        CHILD,
        SIBLING
    }

    override val name: String = "RELTYPE"
    override val values: List<Any> = listOf(relation)
}