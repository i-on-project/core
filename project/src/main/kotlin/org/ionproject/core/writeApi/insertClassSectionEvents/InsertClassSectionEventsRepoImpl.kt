package org.ionproject.core.writeApi.insertClassSectionEvents

import org.ionproject.core.calendarTerm.sql.CalendarTermData
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.CALENDAR_SECTION_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.CALENDAR_TERM_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.CALL_CREATE_EVENT
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.CALL_UPSERT_COURSE
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.CALL_UPSERT_SCHOOL
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.CATEGORY_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.COURSE_ACR_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.COURSE_NAME_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.DESCRIPTION_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.DTEND_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.DTSTART_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.LANGUAGE_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.LOCATION_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.PROGRAMME_ACR_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.PROGRAMME_NAME_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.PROGRAMME_TERM_SIZE_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.SCHOOL_ACR_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.SCHOOL_NAME_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.SUMMARY_PARAM
import org.ionproject.core.writeApi.insertClassSectionEvents.sql.InsertClassSectionEventsData.WEEK_DAYS_PARAM
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.OffsetDateTime

@Component
class InsertClassSectionEventsRepoImpl(
    private val tm: TransactionManager
) : InsertClassSectionEventsRepo {

    override fun insertClassSectionSchoolInfo(
        schoolName: String?,
        schoolAcr: String?,
        programmeName: String?,
        programmeAcr: String?,
        programmeTermSize: Int?,
        calendarTerm: String?
    ): Unit = tm.run(TransactionIsolationLevel.SERIALIZABLE) {
        InsertClassSectionEventsTransactionRepo(it).insertClassSectionSchoolInfo(schoolName, schoolAcr, programmeName, programmeAcr, programmeTermSize, calendarTerm)
    }

    override fun insertClassSectionCourseInfo(
        courseName: String?,
        courseAcr: String?,
        calendarSection: String?,
        calendarTerm: String?,
        language: String
    ): Unit = tm.run(TransactionIsolationLevel.SERIALIZABLE) {
        InsertClassSectionEventsTransactionRepo(it).insertClassSectionCourseInfo(courseName, courseAcr, calendarSection, calendarTerm, language)
    }

    override fun insertClassSectionEvent(
        courseName: String?,
        courseAcr: String?,
        calendarSection: String?,
        calendarTerm: String?,
        summary: String,
        description: String,
        language: String,
        category: Int,
        dtstart: Timestamp?,
        dtend: Timestamp?,
        weekDays: String?,
        location: String?
    ): Unit = tm.run(TransactionIsolationLevel.SERIALIZABLE) {
        InsertClassSectionEventsTransactionRepo(it).insertClassSectionEvent(courseName, courseAcr, calendarSection, calendarTerm, summary, description, language, category, dtstart, dtend, weekDays, location)
    }

    override fun transaction(t: (InsertClassSectionEventsRepo) -> Unit): Boolean {
        tm.run {
            val repo = InsertClassSectionEventsTransactionRepo(it)
            t(repo)
        }
        return true
    }
}

class InsertClassSectionEventsTransactionRepo(
    private val handle: Handle
) : InsertClassSectionEventsRepo {
    override fun insertClassSectionSchoolInfo(
        schoolName: String?,
        schoolAcr: String?,
        programmeName: String?,
        programmeAcr: String?,
        programmeTermSize: Int?,
        calendarTerm: String?
    ) {
        handle
            .createCall(CALL_UPSERT_SCHOOL)
            .bind(SCHOOL_NAME_PARAM, schoolName)
            .bind(SCHOOL_ACR_PARAM, schoolAcr)
            .bind(PROGRAMME_NAME_PARAM, programmeName)
            .bind(PROGRAMME_ACR_PARAM, programmeAcr)
            .bind(PROGRAMME_TERM_SIZE_PARAM, programmeTermSize)
            .bind(CALENDAR_TERM_PARAM, calendarTerm)
            .invoke()
    }

    override fun insertClassSectionCourseInfo(
        courseName: String?,
        courseAcr: String?,
        calendarSection: String?,
        calendarTerm: String?,
        language: String
    ) {
        handle
            .createCall(CALL_UPSERT_COURSE)
            .bind(COURSE_NAME_PARAM, courseName)
            .bind(COURSE_ACR_PARAM, courseAcr)
            .bind(CALENDAR_SECTION_PARAM, calendarSection)
            .bind(CALENDAR_TERM_PARAM, calendarTerm)
            .bind(LANGUAGE_PARAM, language)
            .invoke()
    }

    override fun insertClassSectionEvent(
        courseName: String?,
        courseAcr: String?,
        calendarSection: String?,
        calendarTerm: String?,
        summary: String,
        description: String,
        language: String,
        category: Int,
        dtstart: Timestamp?,
        dtend: Timestamp?,
        weekDays: String?,
        location: String?
    ) {
        handle
            .createCall(CALL_CREATE_EVENT)
            .bind(COURSE_NAME_PARAM, courseName)
            .bind(COURSE_ACR_PARAM, courseAcr)
            .bind(CALENDAR_SECTION_PARAM, calendarSection)
            .bind(CALENDAR_TERM_PARAM, calendarTerm)
            .bind(SUMMARY_PARAM, summary)
            .bind(DESCRIPTION_PARAM, description)
            .bind(LANGUAGE_PARAM, language)
            .bind(CATEGORY_PARAM, category)
            .bind(DTSTART_PARAM, dtstart)
            .bind(DTEND_PARAM, dtend)
            .bind(WEEK_DAYS_PARAM, weekDays)
            .bind(LOCATION_PARAM, location)
            .invoke()
    }

    override fun transaction(t: (InsertClassSectionEventsRepo) -> Unit): Boolean {
        t(this)
        return true
    }

}
