package org.ionproject.core.ingestion.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

enum class ExamCategory {
    TEST,
    EXAM_NORMAL,
    EXAM_ALTERN,
    EXAM_SPECIAL
}

data class ExamScheduleSchool(
    val name: String,
    @JsonProperty("acr")
    val acronym: String
)

data class ExamScheduleProgramme(
    val name: String,
    @JsonProperty("acr")
    val acronym: String
)

data class Exam(
    val name: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val category: ExamCategory,
    val location: List<String>? = null
)

data class ExamSchedule(
    val creationDateTime: String, // TODO: change to Instant
    val retrievalDateTime: String, // TODO: change to Instant
    val school: ExamScheduleSchool,
    val programme: ExamScheduleProgramme,
    val calendarTerm: String,
    val exams: List<Exam> = emptyList()
)