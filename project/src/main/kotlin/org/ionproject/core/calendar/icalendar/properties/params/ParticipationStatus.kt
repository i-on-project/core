package org.ionproject.core.calendar.icalendar.properties.params

class ParticipationStatus(status: Status) : PropertyParameter("PARTSTAT", status) {
    enum class Status(private val altName: String? = null) {
        NEEDS_ACTION("NEEDS-ACTION"),
        ACCEPTED,
        DECLINED,
        TENTATIVE,
        DELEGATED,
        COMPLETED,
        IN_PROCESS("IN-PROCESS");

        override fun toString(): String = altName ?: name
    }
}