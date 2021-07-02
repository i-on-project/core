package org.ionproject.core.ingestion.processor.sql.model

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDateTime

data class CalendarTerm(
    val id: String,
    @ColumnName("start_date")
    val startDate: LocalDateTime,
    @ColumnName("end_date")
    val endDate: LocalDateTime
)

data class RealCalendarTerm(
    val id: String,
    @ColumnName("start_date")
    val startDate: Int,
    @ColumnName("end_date")
    val endDate: Int
)
