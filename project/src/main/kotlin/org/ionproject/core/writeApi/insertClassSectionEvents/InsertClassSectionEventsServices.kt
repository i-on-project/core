package org.ionproject.core.writeApi.insertClassSectionEvents

import org.ionproject.core.writeApi.insertClassSectionEvents.json.OperationParams
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Component

/**
 * The main goal of this class is to unclutter the Controller class.
 */
@Component
class InsertClassSectionEventsServices(private val repo: InsertClassSectionEventsRepo) {

    /**
     * Encapsulate transaction for exception handling.
     *
     * @return errorMessage - *nil* if the transaction completed successfully
     */
    private fun tryExecuteTransaction(transaction: (InsertClassSectionEventsRepo) -> Unit): String? =
        try {
            if (repo.transaction(transaction)) {
                null
            } else {
                "An unknown error has occurred during the execution of the transaction."
            }

        } catch (se: UnableToExecuteStatementException) {
            // PostgreSQL reported an error and the transaction was aborted...
            val ex = se.cause as PSQLException
            ex.serverErrorMessage.message
        }

    /**
     * Using the prepared procedure parameters, execute all required SPs in order.
     *
     * @return errorMessage - whatever [tryExecuteTransaction] returns
     */
    fun insertClassSectionEvents(params: OperationParams): String? = tryExecuteTransaction { sql ->
        sql.insertClassSectionSchoolInfo(
            params.schoolName,
            params.schoolAcr,
            params.programmeName,
            params.programmeAcr,
            params.programmeTermSize,
            params.calendarTerm
        )

        params.courses.forEach { course ->
            sql.insertClassSectionCourseInfo(
                course.name,
                course.acr,
                params.calendarSection,
                params.calendarTerm,
                params.language
            )

            course.events.forEach { event ->
                // Mandatory iCalendar component properties
                // Give default values in case these weren't included in the request
                val eventTitle = event.title ?: "${course.acr} ${event.category}"
                val eventDescription = event.description
                    ?: "Event '${event.category}' during ${params.calendarTerm} for the Class ${params.calendarSection}."

                sql.insertClassSectionEvent(
                    course.name,
                    course.acr,
                    params.calendarSection,
                    params.calendarTerm,
                    eventTitle,
                    eventDescription,
                    params.language,
                    event.category,
                    event.beginTime,
                    event.endTime,
                    event.weekdays,
                    event.location
                )
            }
        }
    }
}
