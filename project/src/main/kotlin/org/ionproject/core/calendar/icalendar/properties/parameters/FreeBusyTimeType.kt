package org.ionproject.core.calendar.icalendar.properties.parameters

class FreeBusyTimeType(type: Type = Type.BUSY) : PropertyParameter("FBTYPE", type) {
    enum class Type(private val altName: String? = null) {
        FREE,
        BUSY,
        BUSY_UNAVAILABLE("BUSY-UNAVAILABLE"),
        BUSY_TENTATIVE("BUSY-TENTATIVE");

        override fun toString(): String = altName ?: name
    }
}