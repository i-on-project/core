package org.ionproject.core.calendar.icalendar.types

class DateTime (
    private val date: Date,
    private val time: Time
) {
    override fun toString(): String {
        return "${date}T$time"
    }
}

class Date(
    private val year: Year,
    private val month: Month,
    private val day: MonthDay
) {
    companion object {
        fun of(year: Int, month: Int, day: Int): Date =
            Date(
                Year(year),
                Month(month),
                MonthDay(day)
            )

    }

    override fun toString(): String {
        return "$year$month$day"
    }
}

class MonthDay(
    private val value: Int
) {
    init {
        if (value < 0 || value > 31) throw IllegalMonthDayException("Month days must be between 0 and 31.")
    }

    override fun toString(): String {
        return String.format("%02d", value)
    }
}

class IllegalMonthDayException(message: String) : IllegalArgumentException(message)

class Month(
    private val value: Int
) {
    init {
        if (value < 0 || value > 12) throw IllegalMonthException("Months must be between 0 and 12.")
    }

    override fun toString(): String {
        return String.format("%02d", value)
    }
}

class Year(
    private val value: Int
) {
    init {
        if (value < 0 || value > 9999) throw IllegalYearException("A year must be between 0 and 9999.")
    }

    override fun toString(): String {
        return String.format("%04d", value)
    }
}

class IllegalYearException(message: String) : IllegalArgumentException(message)

class Time(
    private val hours: Hour,
    private val minutes: Minute,
    private val seconds: Second,
    private val utc: Boolean = true
) {
    companion object {
        fun of(hours: Int, minutes: Int, seconds: Int): Time =
            Time(
                Hour(hours),
                Minute(minutes),
                Second(seconds)
            )

    }

    override fun toString(): String {
        return "$hours$minutes$seconds${if (utc) "Z" else ""}"
    }
}

class Hour(
    private val value: Int
) {
    init {
        if (value < 0 || value > 23) throw IllegalHourException("Hour values can only go from 0-23.")
    }

    override fun toString(): String {
        return String.format("%02d", value)
    }
}

class IllegalHourException(message: String) : IllegalArgumentException(message)

class Minute(
    private val value: Int
) {
    init {
        if (value < 0 || value > 59) throw IllegalMinuteException("Minute values can only go from 0-59.")
    }

    override fun toString(): String {
        return String.format("%02d", value)
    }
}

class IllegalMinuteException(message: String) : IllegalArgumentException(message)

class Second(
    private val value: Int
) {
    init {
        if (value < 0 || value > 60) throw IllegalSecondException("Second values can only go from 0-60.")
    }

    override fun toString(): String {
        return String.format("%02d", value)
    }
}

class IllegalSecondException(message: String) : IllegalArgumentException(message)
