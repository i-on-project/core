package org.ionproject.core.calendarTerm.model

import org.ionproject.core.klass.model.Klass
import java.time.LocalDateTime

class CalendarTerm(
    val calTermId: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val classes: MutableList<Klass> = mutableListOf()
) // Classes is not always necessary, when not the default is empty list

/*
 *https://jdbc.postgresql.org/documentation/head/8-date-time.html
 * OffsetDateTime, correspondent of timestamp with timezone
 */
