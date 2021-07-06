package org.ionproject.core.ingestion.processor.sql.model

import org.ionproject.core.calendarTerm.model.ExamSeason
import java.time.LocalTime

data class ExamSeasonInput(
    val id: Int = 0,
    val calendarTerm: String,
    val description: String,
    val startDate: Int,
    val endDate: Int
)

fun ExamSeason.toCalendarInstants() = listOf(
    CalendarInstant(startDate, LocalTime.MIDNIGHT),
    CalendarInstant(endDate, LocalTime.MIDNIGHT)
)

fun ExamSeason.toExamSeasonInput(start: Int, end: Int) = ExamSeasonInput(
    id = id,
    calendarTerm = calendarTerm,
    description = description,
    startDate = start,
    endDate = end
)
