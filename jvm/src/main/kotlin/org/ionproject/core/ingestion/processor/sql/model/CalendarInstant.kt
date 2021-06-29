package org.ionproject.core.ingestion.processor.sql.model

import java.time.LocalDate
import java.time.LocalTime

data class CalendarInstant(
    val date: LocalDate,
    val time: LocalTime
)
