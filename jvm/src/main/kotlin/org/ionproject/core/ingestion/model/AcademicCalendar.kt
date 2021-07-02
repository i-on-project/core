package org.ionproject.core.ingestion.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDate

data class AcademicCalendarSchool(
    val name: String,
    @JsonProperty("acr")
    val acronym: String
)

data class AcademicInterruption(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class AcademicEvaluation(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val duringLectures: Boolean
)

data class AcademicCurricularTerm(
    val id: Int
)

data class AcademicDetail(
    val name: String,
    val curricularTerm: List<AcademicCurricularTerm> = emptyList(),
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class AcademicEvent(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class AcademicCalendarTerm(
    val calendarTerm: String,
    val interruptions: List<AcademicInterruption> = emptyList(),
    val evaluations: List<AcademicEvaluation> = emptyList(),
    val lectures: List<AcademicDetail> = emptyList(),
    val otherEvents: List<AcademicEvent> = emptyList()
)

data class AcademicCalendar(
    val creationDateTime: Instant,
    val retrievalDateTime: Instant,
    val school: AcademicCalendarSchool,
    val language: String,
    val terms: List<AcademicCalendarTerm> = emptyList()
)
