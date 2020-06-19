package org.ionproject.core.writeApi.insertClassSectionEvents

import java.sql.Timestamp

interface InsertClassSectionEventsRepo {
  fun insertClassSectionSchoolInfo(
    schoolName: String?,
    schoolAcr: String?,
    programmeName: String?,
    programmeAcr: String?,
    programmeTermSize: Int?,
    calendarTerm: String?
  )

  fun insertClassSectionCourseInfo(
    courseName: String?,
    courseAcr: String?,
    calendarSection: String?,
    calendarTerm: String?,
    language: String,
    category: String
  )

  fun insertClassSectionEvent(
    courseName: String?,
    courseAcr: String?,
    calendarSection: String?,
    calendarTerm: String?,
    summary: String?,
    description: String?,
    language: String,
    category: String,
    dtstart: Timestamp?,
    dtend: Timestamp?,
    weekDays: String?
  )

  fun transaction(t: (InsertClassSectionEventsRepo) -> Unit): Boolean
}
