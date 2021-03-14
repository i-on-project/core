package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.calendar.sql.CalendarComponentMapper
import org.ionproject.core.calendar.sql.CalendarData
import org.ionproject.core.common.Uri
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Repository
import org.springframework.util.MultiValueMap

@Repository
class CalendarRepoImpl(
    private val transactionManager: TransactionManager,
    private val componentMapper: CalendarComponentMapper
) : CalendarRepo {

    override fun getClassCalendar(
        courseId: Int,
        calendarTerm: String,
        filters: MultiValueMap<String, String>
    ): Calendar? {
        val components = transactionManager.run {
            checkIfClassIsValid(it, courseId, calendarTerm)

            CalendarData.calendarFromClassQuery(it, courseId, calendarTerm, filters)
                .map(componentMapper)
                .list()
        }

        return Calendar(
            ProductIdentifier(Uri.forKlassByCalTerm(courseId, calendarTerm).toString()),
            Version(),
            null,
            null,
            components
        )
    }

    override fun getClassSectionCalendar(
        courseId: Int,
        calendarTerm: String,
        classSectionId: String,
        filters: MultiValueMap<String, String>
    ): Calendar? {
        val components = transactionManager.run {
            checkIfClassSectionIsValid(it, courseId, calendarTerm, classSectionId)

            CalendarData.calendarFromClassSectionQuery(it, courseId, calendarTerm, classSectionId, filters)
                .map(componentMapper)
                .list()
        }

        return Calendar(
            ProductIdentifier(Uri.forClassSectionById(courseId, calendarTerm, classSectionId).toString()),
            Version(),
            null,
            null,
            components
        )
    }

    override fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar? {
        try {
            val component = transactionManager.run {
                checkIfClassIsValid(it, courseId, calendarTerm)

                it.createQuery(CalendarData.CALENDAR_COMPONENT_FROM_CLASS_QUERY)
                    .bind(CalendarData.COURSE_ID, courseId)
                    .bind(CalendarData.CAL_TERM, calendarTerm)
                    .bind(CalendarData.UID, componentId)
                    .map(componentMapper)
                    .one()
            }

            return Calendar(
                ProductIdentifier(Uri.forKlassByCalTerm(courseId, calendarTerm).toString()),
                Version(),
                null,
                null,
                mutableListOf(component)
            )
        } catch (e: IllegalStateException) {
            return null
        }
    }

    override fun getClassSectionCalendarComponent(
        courseId: Int,
        calendarTerm: String,
        classSectionId: String,
        componentId: Int
    ): Calendar? {
        try {
            val component = transactionManager.run {
                checkIfClassSectionIsValid(it, courseId, calendarTerm, classSectionId)

                it.createQuery(CalendarData.CALENDAR_COMPONENT_FROM_CLASS_SECTION_QUERY)
                    .bind(CalendarData.COURSE_ID, courseId)
                    .bind(CalendarData.CAL_TERM, calendarTerm)
                    .bind(CalendarData.ID, classSectionId)
                    .bind(CalendarData.UID, componentId)
                    .map(componentMapper)
                    .one()
            }

            return Calendar(
                ProductIdentifier(Uri.forClassSectionById(courseId, calendarTerm, classSectionId).toString()),
                Version(),
                null,
                null,
                mutableListOf(component)
            )
        } catch (e: IllegalStateException) {
            return null
        }
    }

    private fun checkIfClassIsValid(handle: Handle, courseId: Int, calendarTerm: String) {
        val count = handle
            .createQuery(CalendarData.CHECK_IF_CLASS_EXISTS)
            .bind(CalendarData.CAL_TERM, calendarTerm)
            .bind(CalendarData.ID, courseId)
            .mapTo(Integer::class.java)
            .one()
            .toInt()

        if (count == 0) {
            throw ResourceNotFoundException("There is no class for the course with id $courseId at the calendar term $calendarTerm.")
        }
    }

    private fun checkIfClassSectionIsValid(
        handle: Handle,
        courseId: Int,
        calendarTerm: String,
        classSectionId: String
    ) {
        val count = handle
            .createQuery(CalendarData.CHECK_IF_CLASS_SECTION_EXISTS)
            .bind(CalendarData.CAL_TERM, calendarTerm)
            .bind(CalendarData.ID, courseId)
            .bind(CalendarData.CAS_ID, classSectionId)
            .mapTo(Integer::class.java)
            .one()
            .toInt()

        if (count == 0) {
            throw ResourceNotFoundException("There is no classSection $classSectionId for the course with id $courseId at the calendar term $calendarTerm.")
        }
    }
}
