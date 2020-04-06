package org.ionproject.core.calendar.icalendar.properties.parameters

class ParticipationRole(value: Role = Role.REQ_PARTICIPANT) : PropertyParameter("ROLE", value) {
    enum class Role(private val altName: String? = null) {
        CHAIR,
        REQ_PARTICIPANT("REQ-PARTICIPANT"),
        OPT_PARTICIPANT("OPT-PARTICIPANT"),
        NON_PARTICIPANT;

        override fun toString(): String = altName ?: name
    }
}