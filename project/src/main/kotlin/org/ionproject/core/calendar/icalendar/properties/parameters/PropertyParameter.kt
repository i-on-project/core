package org.ionproject.core.calendar.icalendar.properties.parameters

abstract class PropertyParameter private constructor(
    private val name: String,
    private val values: List<String>
) {
    constructor(name: String, vararg values: Any) : this(name, values.map{ it.toString().toPropertyParameterText() })

    override fun toString(): String {
        val values = this.values.joinToString()
        return ";$name=$values"
    }
}

fun String.toPropertyParameterText() : String {
    if ((contains(',') || contains(':') || contains(';')) && !startsAndEndsWith("\"")) {
        return "\"$this\""
    }
    return this
}

fun String.startsAndEndsWith(str: String) : Boolean = str.startsWith(str) && str.endsWith(str)