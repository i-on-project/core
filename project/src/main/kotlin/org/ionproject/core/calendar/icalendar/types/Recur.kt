package org.ionproject.core.calendar.icalendar.types

class Recur private constructor(
    val frequency: Frequency = Frequency.WEEKLY,
    val until: ICalendarDataType? = null,
    val count: Int? = null,
    val interval: Int? = null,
    val byDay: List<WeekDay>? = null
) : ICalendarDataType {

    constructor(interval: Int? = null, byDay: List<WeekDay>?) : this(Frequency.WEEKLY, null, null, interval, byDay)
    constructor(until: Date?, interval: Int? = null, byDay: List<WeekDay>?) : this(
        Frequency.WEEKLY,
        until = until,
        interval = interval,
        byDay = byDay
    )

    constructor(until: DateTime?, interval: Int? = null, byDay: List<WeekDay>?) : this(
        Frequency.WEEKLY,
        until = until,
        interval = interval,
        byDay = byDay
    )

    constructor(count: Int?, interval: Int? = null, byDay: List<WeekDay>?) : this(Frequency.WEEKLY, count = count, interval = interval, byDay = byDay)

    override val value: Any
        get() = toString()

    override val name: String = "RECUR"

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("FREQ=$frequency")
        if (until != null) {
            builder.append(";UNTIL=$until")
        }
        if (count != null) {
            builder.append(";COUNT=$count")
        }
        if (interval != null) {
            builder.append(";INTERVAL=$interval")
        }
        if (byDay != null) {
            builder.append(";BYDAY=${byDay.joinToString(",")}")
        }
        return builder.toString()
    }
}

private const val MAX_WEEK_NUMBER = 53

class Week(
    val number: Int,
    val positive: Boolean = true
) {
    init {
        if (number < 1 || number > MAX_WEEK_NUMBER) throw IllegalWeekException("A week must be between 1 and $MAX_WEEK_NUMBER")
    }

    override fun toString(): String = "${if (positive) '+' else '-'}$number"
}

class IllegalWeekException(s: String) : Exception(s)

class WeekDay(
    val value: Weekday,
    val week: Week? = null
) {
    enum class Weekday(val longName: String) {
        SU("Sunday"),
        MO("Monday"),
        TU("Tuesday"),
        WE("Wednesday"),
        TH("Thursday"),
        FR("Friday"),
        SA("Saturday");
    }

    override fun toString(): String {
        return if (week != null) {
            "$week$value"
        } else "$value"
    }
}

enum class Frequency {
    SECONDLY, MINUTELY, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY;
}