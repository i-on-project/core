package org.ionproject.core.calendar.icalendar.properties.parameters

class ParticipationRole(value: Role = Role.REQ_PARTICIPANT) : PropertyParameter {
    enum class Role(private val altName: String? = null) {
        CHAIR,
        REQ_PARTICIPANT("REQ-PARTICIPANT"),
        OPT_PARTICIPANT("OPT-PARTICIPANT"),
        NON_PARTICIPANT;

        override fun toString(): String = altName ?: name
    }

    override val name: String = "ROLE"
    override val values: List<Any> = listOf(value)
}