package org.ionproject.core.calendar.icalendar.properties.parameters

class ParticipationStatus(status: Status) : PropertyParameter {

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

  override val name: String = "PARTSTAT"
  override val values: List<Any> = listOf(status)
}