package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface CalendarRepo {
    fun getClassCalendar(courseId: Int, calendarTerm: String): Calendar?
    fun getClassSectionCalendar(courseId: Int, calendarTerm: String, classSectionId: Int): Calendar?
    fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar?
    fun getClassSectionCalendarComponent(courseId: Int, calendarTerm: String, classSectionId: Int, componentId: Int): Calendar?
}

@Repository
class CalendarRepoImpl(
    private val transactionManager: TransactionManager,
    private val componentMapper: CalendarData.CalendarComponentMapper
) : CalendarRepo {

    override fun getClassCalendar(courseId: Int, calendarTerm: String): Calendar? {
        val components = transactionManager.run {
            it.createQuery(CalendarData.CALENDAR_FROM_CLASS_QUERY)
                .bind("courseId", courseId)
                .bind("term", calendarTerm)
                .map(componentMapper)
                .list()
        } ?: mutableListOf()

        return Calendar(
            ProductIdentifier("/$courseId/classes/$calendarTerm"),
            Version(),
            null,
            null,
            components
        )
    }

    override fun getClassSectionCalendar(courseId: Int, calendarTerm: String, classSectionId: Int): Calendar? {
        val components = transactionManager.run {
            it.createQuery(CalendarData.CALENDAR_FROM_CLASS_QUERY)
                .bind("courseId", courseId)
                .bind("term", calendarTerm)
                .bind("classSectionId", classSectionId)
                .map(componentMapper)
                .list()
        } ?: mutableListOf()

        return Calendar(
            ProductIdentifier("/$courseId/classes/$calendarTerm/$classSectionId"),
            Version(),
            null,
            null,
            components
        )
    }

    override fun getClassCalendarComponent(courseId: Int, calendarTerm: String, componentId: Int): Calendar? {
        val component = transactionManager.run {
            it.createQuery(CalendarData.CALENDAR_COMPONENT_FROM_CLASS_QUERY)
                .bind("courseId", courseId)
                .bind("term", calendarTerm)
                .bind("componentId", componentId)
                .map(componentMapper)
                .one()
        } ?: return null

        return Calendar(
            ProductIdentifier("/$courseId/classes/$calendarTerm"),
            Version(),
            null,
            null,
            mutableListOf(
                component
            )
        )
    }

    override fun getClassSectionCalendarComponent(courseId: Int, calendarTerm: String, classSectionId: Int, componentId: Int): Calendar? {
        val component = transactionManager.run {
            it.createQuery(CalendarData.CALENDAR_COMPONENT_FROM_CLASS_SECTION_QUERY)
                .bind("courseId", courseId)
                .bind("term", calendarTerm)
                .bind("classSectionId", classSectionId)
                .bind("componentId", componentId)
                .map(componentMapper)
                .one()
        } ?: return null

        return Calendar(
            ProductIdentifier("/$courseId/classes/$calendarTerm/$classSectionId"),
            Version(),
            null,
            null,
            mutableListOf(
                component
            )
        )
    }
}