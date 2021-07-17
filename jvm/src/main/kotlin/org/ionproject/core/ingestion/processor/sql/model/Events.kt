package org.ionproject.core.ingestion.processor.sql.model

import org.ionproject.core.ingestion.model.TimetableClassSection
import org.ionproject.core.ingestion.model.TimetableEventCategory
import java.time.LocalTime

object EventLanguage {

    const val PORTUGUESE = 1
    const val ENGLISH = 2
}

object EventCategory {

    const val EXAM = 1
    const val LECTURE = 2
    const val LABORATORY = 3
    const val WARNING = 4
    const val DEADLINE = 5
}

object EventDateType {

    const val BINARY = 1
    const val BOOLEAN = 2
    const val CAL_ADDRESS = 3
    const val DATE = 4
    const val DATE_TIME = 5
    const val DURATION = 6
}

data class RealEventWithDateReferences(
    val calendarId: Int,
    val summary: List<String>,
    val summaryLanguage: List<Int>,
    val description: List<String>,
    val descriptionLanguage: List<Int>,
    val category: List<Int>,
    val startInstant: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dateType: Int,
    val location: String?,
    val weekday: String,
    val until: Int
)

fun TimetableClassSection.toEvents(
    calendarId: Int,
    acronym: String,
    startInstant: Int,
    endInstant: Int
): List<RealEventWithDateReferences> {
    val languages = listOf(EventLanguage.PORTUGUESE, EventLanguage.ENGLISH)
    val dateType = EventDateType.DATE_TIME

    return events.map {
        RealEventWithDateReferences(
            calendarId,
            listOf(acronym, acronym),
            languages,
            listOf(section, section),
            languages,
            it.category.toEventCategory(),
            startInstant,
            it.beginTime,
            it.beginTime.plus(it.duration),
            dateType,
            it.location?.get(0),
            it.weekdays.name,
            endInstant
        )
    }
}

fun TimetableEventCategory.toEventCategory(): List<Int> {
    return when (this) {
        TimetableEventCategory.LECTURE -> listOf(EventCategory.LECTURE)
        TimetableEventCategory.LECTURE_PRACTICE -> listOf(EventCategory.LECTURE, EventCategory.LABORATORY)
        TimetableEventCategory.LAB, TimetableEventCategory.PRACTICE -> listOf(EventCategory.LABORATORY)
    }
}
