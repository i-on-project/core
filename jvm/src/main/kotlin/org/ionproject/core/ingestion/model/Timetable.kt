package org.ionproject.core.ingestion.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

enum class TimetableEventCategory {
    LECTURE,
    PRACTICE,
    LAB,
    LECTURE_PRACTICE
}

enum class TimetableEventWeekday {
    MO,
    TU,
    WE,
    TH,
    FR,
    SA,
    SU
}

data class TimetableEvent(
    val category: TimetableEventCategory,
    val location: List<String>? = null,
    val beginTime: LocalTime,
    @JsonDeserialize(converter = StringToDurationConverter::class)
    val duration: Duration,
    val weekdays: TimetableEventWeekday
)

data class TimetableInstructor(
    val name: String,
    val category: TimetableEventCategory
)

data class TimetableClassSection(
    val section: String,
    val curricularTerm: Int,
    val events: List<TimetableEvent> = emptyList(),
    val instructors: List<TimetableInstructor> = emptyList()
)

data class TimetableClass(
    @JsonProperty("acr")
    val acronym: String,
    val name: String = acronym,
    val sections: List<TimetableClassSection> = emptyList()
)

data class Timetable(
    val creationDateTime: Instant,
    val retrievalDateTime: Instant,
    val school: String,
    val programme: String,
    val calendarTerm: String,
    val classes: List<TimetableClass> = emptyList()
)

private class StringToDurationConverter : StdConverter<String, Duration>() {

    override fun convert(value: String?): Duration {
        val str = value!!
        val strSplit = str.split(":")
        val hours = strSplit[0].toLong()
        val minutes = strSplit[1].toLong()

        return Duration.ofHours(hours)
            .plusMinutes(minutes)
    }
}
