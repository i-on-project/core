package org.ionproject.core.calendar.icalendar.properties.parameters

class FormatType(typeName: String, subTypeName: String) : PropertyParameter {
    override val name: String = "FMTTYPE"
    override val values: List<Any> = listOf("$typeName/$subTypeName")
}
