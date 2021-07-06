package org.ionproject.core.calendarTerm.model

import java.time.LocalDate

data class ExamSeason(
    val id: Int = 0,
    val calendarTerm: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
