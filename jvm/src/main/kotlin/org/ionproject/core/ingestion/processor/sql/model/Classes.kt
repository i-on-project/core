package org.ionproject.core.ingestion.processor.sql.model

import org.ionproject.core.ingestion.model.TimetableClass

data class RealClass(
    val id: Int,
    val courseId: Int,
    val calendarTerm: String,
    val calendar: Int
)

fun TimetableClass.toRealClass(courseId: Int, calendarTerm: String) = RealClass(
    0,
    courseId,
    calendarTerm,
    0
)

data class RealClassSection(
    val id: String,
    val classId: Int,
    val calendar: Int
)
