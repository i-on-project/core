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
    val details: List<AcademicDetail> = emptyList(),
    val otherEvents: List<AcademicEvent> = emptyList()
)

data class AcademicCalendar(
    val creationDateTime: String, // TODO: change to Instant
    val retrievalDateTime: String, // TODO: change to Instant
    val school: AcademicCalendarSchool,
    val language: String?, // TODO: This shouldn't be nullable
    val terms: List<AcademicCalendarTerm> = emptyList()
)