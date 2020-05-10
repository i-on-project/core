package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.common.Uri
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface CalendarRepo {
    fun getClassCalendar(
            courseId: Int,
            calendarTerm: String,
            type: Char?,
            startBefore: String?,
            startAfter: String?,
            endBefore: String?,
            endAfter: String?,
            summary: String?
    ): Calendar?

    fun getClassSectionCalendar(
            courseId: Int,
            calendarTerm: String,
            classSectionId: String,
            type: Char?,
            startBefore: String?,
            startAfter: String?,
            endBefore: String?,
            endAfter: String?,
            summary: String?
    ): Calendar?

    fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar?
    fun getClassSectionCalendarComponent(
            courseId: Int,
            calendarTerm: String,
            classSectionId: String,
            componentId: Int
    ): Calendar?
}

@Repository
class CalendarRepoImpl(
        private val transactionManager: TransactionManager,
        private val componentMapper: CalendarData.CalendarComponentMapper
) : CalendarRepo {

    override fun getClassCalendar(
            courseId: Int,
            calendarTerm: String,
            type: Char?,
            startBefore: String?,
            startAfter: String?,
            endBefore: String?,
            endAfter: String?,
            summary: String?
    ): Calendar? {
        val components = transactionManager.run {
            it.createQuery(CalendarData.CALENDAR_FROM_CLASS_QUERY)
                    .bind(CalendarData.COURSE, courseId)
                    .bind(CalendarData.TERM, calendarTerm)
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

        TODO("use query params. Need query builder from https://github.com/i-on-project/core/issues/76")
    }

    override fun getClassSectionCalendar(
            courseId: Int,
            calendarTerm: String,
            classSectionId: String,
            type: Char?,
            startBefore: String?,
            startAfter: String?,
            endBefore: String?,
            endAfter: String?,
            summary: String?
    ): Calendar? {
        val components = transactionManager.run {
            it.createQuery(CalendarData.CALENDAR_FROM_CLASS_SECTION_QUERY)
                    .bind(CalendarData.COURSE, courseId)
                    .bind(CalendarData.TERM, calendarTerm)
                    .bind(CalendarData.ID, classSectionId)
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

        TODO("use query params. Need query builder from https://github.com/i-on-project/core/issues/76")
    }

    override fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar? {
        try {
            val component = transactionManager.run {
                it.createQuery(CalendarData.CALENDAR_COMPONENT_FROM_CLASS_QUERY)
                        .bind(CalendarData.COURSE, courseId)
                        .bind(CalendarData.TERM, calendarTerm)
                        .bind(CalendarData.UID, componentId)
                        .map(componentMapper)
                        .one()
            }

            return Calendar(
                    ProductIdentifier(Uri.forKlassByCalTerm(courseId, calendarTerm).toString()),
                    Version(),
                    null,
                    null,
                    mutableListOf(
                            component
                    )
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
                it.createQuery(CalendarData.CALENDAR_COMPONENT_FROM_CLASS_SECTION_QUERY)
                        .bind(CalendarData.COURSE, courseId)
                        .bind(CalendarData.TERM, calendarTerm)
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
                    mutableListOf(
                            component
                    )
            )
        } catch (e: IllegalStateException) {
            return null
        }

    }
}
