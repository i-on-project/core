package org.ionproject.core.common.model

import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.klass.Klass
import java.time.OffsetDateTime

class CalendarTerm(
        val calTermId: String,
        val startDate: OffsetDateTime,
        val endDate: OffsetDateTime,
        val classes : MutableList<Klass> = mutableListOf())  //Classes is not always necessary, when not the default is empty list

/*
 *https://jdbc.postgresql.org/documentation/head/8-date-time.html
 * OffsetDateTime, correspondent of timestamp with timezone
 */