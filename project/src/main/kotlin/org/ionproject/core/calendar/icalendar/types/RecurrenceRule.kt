package org.ionproject.core.calendar.icalendar.types

class Recur(
    val frequency: Frequency,
    val until: Date? = null,
    val count: Int? = null,
    val interval: Int? = null,
    val bySecond: List<Int>? = null,
    val byMinute: List<Int>? = null,
    val byHour: List<Int>? = null,
    val byDay: List<WeekDay>? = null,
    val byMonthDay: List<DayOfMonth>? = null,
    val byYearDay: List<YearDay>? = null,
    val byWeekNo: List<Week>? = null,
    val byMonth: List<MonthNumber>? = null,
    val bySetPos: List<YearDay>? = null,
    val weekStart: WeekDay.Weekday = WeekDay.Weekday.SU
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.appendIfNotNull(frequency, until, count, interval, bySecond, byMinute, byHour, byDay, byMonthDay,
            byYearDay,
            byWeekNo, byMonth, bySetPos, weekStart)
        return builder.toString()
    }
}

private fun StringBuilder.appendIfNotNull(vararg objs: Any?) {
    for(obj in objs) {
        if (obj != null) this.append(obj.toString())
    }
}

private const val MAX_WEEK_NUMBER = 53

class Week(
    val number: Int,
    val positive: Boolean = true
) {
    init {
        if (number < 1 || number > MAX_WEEK_NUMBER) throw IllegalWeekException(
            "A week must be between 1 and " +
                    "$MAX_WEEK_NUMBER"
        )
    }
}
class IllegalWeekException(s: String) : Exception(s)

private const val MAX_MONTH_NUMBER = 12
class MonthNumber(
    val number: Int,
    val positive: Boolean = true
) {
    init {
        if (number < 1 || number > MAX_MONTH_NUMBER) throw IllegalMonthException(
            "A week must be between 1 and " +
                    "$MAX_MONTH_NUMBER"
        )
    }
}
class IllegalMonthException(s: String) : Exception(s)

private const val MAX_YEAR_DAY = 366
class YearDay(
    val value: Int,
    val positive: Boolean = true
) {
    init {
        if (value < 1 || value > MAX_YEAR_DAY) throw IllegalDayException("A year day must be between 1 and $MAX_YEAR_DAY.")
    }
}

class DayOfMonth(
    val value: Int,
    val positive: Boolean = true
) {
    init {
        if (value < 1 || value > 31) throw IllegalDayException("A month day must be between 1 and 31.")
    }

}

class IllegalDayException(s: String) : Exception(s)

class WeekDay(
    val value: Weekday,
    val week: Week
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

}

enum class Frequency {
    SECONDLY, MINUTELY, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY;

    override fun toString(): String {
        return "FREQ=$name"
    }
}