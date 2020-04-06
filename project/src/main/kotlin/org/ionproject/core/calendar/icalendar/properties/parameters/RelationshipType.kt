package org.ionproject.core.calendar.icalendar.properties.parameters

class RelationshipType(relation: Type = Type.PARENT) : PropertyParameter("RELTYPE", relation) {
    enum class Type {
        PARENT,
        CHILD,
        SIBLING
    }
}