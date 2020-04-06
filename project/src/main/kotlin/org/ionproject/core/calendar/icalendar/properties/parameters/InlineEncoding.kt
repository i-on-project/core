package org.ionproject.core.calendar.icalendar.properties.parameters

class InlineEncoding(encodingType: Type) : PropertyParameter("ENCODING", encodingType) {
    enum class Type(private val altName: String? = null) {
        EIGHT_BIT("8BIT"),
        BASE64;

        override fun toString(): String = altName ?: name
    }
}