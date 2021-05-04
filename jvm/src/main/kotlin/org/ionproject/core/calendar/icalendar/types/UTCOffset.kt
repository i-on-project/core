package org.ionproject.core.calendar.icalendar.types

class UTCOffset(
    private val adding: Boolean,
    private val hours: Time.Hour,
    private val minutes: Time.Minute,
    private val seconds: Time.Second? = null
) : ICalendarDataType {
    companion object {
        private const val name = "UTC-OFFSET"
    }

    override val value: Any
        get() = toString()

    override val name: String
        get() = Companion.name

    override fun toString(): String = "${if (adding) '+' else '-'}$hours$minutes${seconds ?: ""}"
}
