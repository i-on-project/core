package org.ionproject.core.calendarTerm.model

import org.ionproject.core.klass.model.Klass
import java.time.LocalDateTime

class CalendarTerm(
    val calTermId: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val examSeasons: MutableList<ExamSeason> = mutableListOf(),
    val classes: MutableList<Klass> = mutableListOf()
)
