package org.ionproject.core.calendar.icalendar.types

abstract class DurationType(
    val positive: Boolean = true
) {
    override fun toString(): String {
        return "${if(positive) "+" else "-"}P"
    }
}

class DurationWeek(
    val weeks: Int,
    positive: Boolean = true
) : DurationType(positive) {
    override fun toString(): String {
        return "${super.toString()}${weeks}W"
    }
}

class DurationDate(
    val days: Int,
    val time: DurationTime,
    positive: Boolean = true
) : DurationType(positive) {
    override fun toString(): String {
        return "${super.toString()}${days}D$time"
    }
}

class DurationTime(
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    positive: Boolean = true
) : DurationType() {
    override fun toString(): String {
        return "${hours}H${minutes}M${seconds}S"
    }
}
