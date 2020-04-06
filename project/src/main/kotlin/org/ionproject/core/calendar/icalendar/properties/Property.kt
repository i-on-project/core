package org.ionproject.core.calendar.icalendar.properties

import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter

abstract class Property(
    val values: List<Any>,
    val parameters: List<PropertyParameter> = emptyList()
) {
    constructor(
        value: List<Any>,
        vararg parameters: PropertyParameter?
    ) : this(value, listOfNotNull(*parameters))

    constructor(
        value: Any,
        vararg parameters: PropertyParameter?
    ) : this(listOf(value), listOfNotNull(*parameters))

    abstract val name: String

    override fun toString(): String {
        val parameters = this.parameters.joinToString(";")
        val values = this.values.joinToString(",")

        return "$name$parameters:$values\r\n"
    }
}

