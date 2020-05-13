package org.ionproject.core.calendar.icalendar.properties.parameters

class FreeBusyTimeType(type: Type = Type.BUSY) : PropertyParameter {
  enum class Type(private val altName: String? = null) {
    FREE,
    BUSY,
    BUSY_UNAVAILABLE("BUSY-UNAVAILABLE"),
    BUSY_TENTATIVE("BUSY-TENTATIVE");

    override fun toString(): String = altName ?: name
  }

  override val name: String = "FBTYPE"
  override val values: List<Any> = listOf(type)
}