package org.ionproject.core.calendar.icalendar.types

abstract class Duration private constructor(
    val adding: Boolean
) : ICalendarDataType {
    companion object {
        private const val name = "DURATION"

        operator fun invoke(weeks: Int = 0, days: Int = 0, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, adding: Boolean = true): Duration {
            if (weeks > 0) return DurationWeek(weeks, adding)
            if (days > 0) {
                return DurationDate(days, hours, minutes, seconds, adding)
            }
            return DurationTime(hours, minutes, seconds, adding)
        }
    }

    override val name: String
        get() = Companion.name

    override fun toString(): String {
        return "${if (adding) "" else "-"}P"
    }

    private class DurationDate(private val day: DurationDay, private val time: DurationTime? = null, adding: Boolean) : Duration(adding) {
        class DurationDay(val value: Int) {
            init {
                if (value <= 0) throw IllegalArgumentException("Day value must be greater than 0")
            }

            override fun toString(): String {
                return "${value}D"
            }
        }

        override fun toString(): String {
            return if (time != null) "$day$time"
            else "$day"
        }

        companion object {
            operator fun invoke(days: Int, hours: Int, minutes: Int, seconds: Int, adding: Boolean) : DurationDate {
                return DurationDate(
                    DurationDay(days),
                    if (hours > 0 || minutes > 0 || seconds > 0) {
                        DurationTime(hours, minutes, seconds, adding)
                    } else null,
                    adding
                )
            }
        }
    }

    private class DurationTime(private val subTime: DurationSubTime, adding: Boolean) : Duration(adding) {
        abstract class DurationSubTime {
            class DurationHour(private val value: Int, val minutes: DurationMinute?) : DurationSubTime() {
                override fun toString(): String {
                    return if (minutes != null) "${value}H$minutes"
                    else "${value}H"
                }
            }

            class DurationMinute(private val value: Int, val seconds: DurationSecond?) : DurationSubTime() {
                override fun toString(): String {
                    return if (seconds != null) "${value}M$seconds"
                    else "${value}M"
                }
            }

            class DurationSecond(private val value: Int) : DurationSubTime() {
                override fun toString(): String {
                    return "${value}S"
                }
            }

            companion object {
                operator fun invoke(hours: Int, minutes: Int, seconds: Int): DurationSubTime {
                    if (hours > 0) {
                        if (seconds > 0) {
                            return DurationHour(
                                hours,
                                DurationMinute(
                                    minutes,
                                    DurationSecond(seconds)
                                )
                            )
                        }
                        if (minutes > 0) {
                            return DurationHour(
                                hours,
                                DurationMinute(
                                    minutes,
                                    null
                                )
                            )
                        }
                        return DurationHour(hours, null)
                    } else {
                        if (seconds > 0) {
                            return DurationMinute(
                                minutes,
                                DurationSecond(seconds)
                            )
                        }
                        if (minutes > 0) {
                            return DurationMinute(
                                minutes,
                                null
                            )
                        }
                        throw IllegalArgumentException("")
                    }
                }
            }
        }

        companion object {
            operator fun invoke(hours: Int, minutes: Int, seconds: Int, adding: Boolean): DurationTime = DurationTime(DurationSubTime(hours, minutes, seconds), adding)
        }

        override fun toString(): String = "T$subTime"
    }

    private class DurationWeek(val value: Int, adding: Boolean) : Duration(adding) {
        override fun toString(): String = "${value}W"
    }
}