package org.ionproject.core.readApi.calendar.icalendar.types

import org.ionproject.core.toInt

class Time private constructor(
    val hours: Hour,
    val minutes: Minute,
    val seconds: Second,
    val utc: Boolean = true
) : ICalendarDataType {
    companion object {
        private const val name: String = "TIME"

        fun of(hours: Int, minutes: Int, seconds: Int, utc: Boolean = true): Time =
            Time(
                Hour(hours),
                Minute(minutes),
                Second(seconds),
                utc
            )

        operator fun invoke(hours: Int, minutes: Int, seconds: Int, utc: Boolean = true): Time =
            of(hours, minutes, seconds, utc)

        fun parse(timeComp: CharSequence): Time =
            Time(
                timeComp.subSequence(0, 2).toInt(),
                timeComp.subSequence(2, 4).toInt(),
                timeComp.subSequence(4, 6).toInt(),
                timeComp.length == 7 && timeComp[6] == 'Z'
            )
    }

    override val value: Any
        get() = toString()

    override val name: String
        get() = Companion.name

    override fun toString(): String {
        return "$hours$minutes$seconds${if (utc) "Z" else ""}"
    }

    class Hour(
        private val value: Int
    ) {
        init {
            if (value < 0 || value > 23) throw IllegalArgumentException("Hour values can only go from 0-23.")
        }

        override fun toString(): String {
            return String.format("%02d", value)
        }
    }

    class Minute(
        private val value: Int
    ) {
        init {
            if (value < 0 || value > 59) throw IllegalArgumentException("Minute values can only go from 0-59.")
        }

        override fun toString(): String {
            return String.format("%02d", value)
        }
    }

    class Second(
        private val value: Int
    ) {
        init {
            if (value < 0 || value > 60) throw IllegalArgumentException("Second values can only go from 0-60.")
        }

        override fun toString(): String {
            return String.format("%02d", value)
        }
    }
}