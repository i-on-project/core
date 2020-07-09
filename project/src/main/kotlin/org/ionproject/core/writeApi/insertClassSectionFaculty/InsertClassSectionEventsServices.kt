package org.ionproject.core.writeApi.insertClassSectionFaculty

import org.ionproject.core.writeApi.insertClassSectionFaculty.json.OperationParams
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Component

/**
 * The main goal of this class is to unclutter the Controller class.
 */
@Component
class InsertClassSectionFacultyServices(private val repo: InsertClassSectionFacultyRepo) {

    /**
     * Encapsulate transaction for exception handling.
     *
     * @return errorMessage - *nil* if the transaction completed successfully
     */
    private fun tryExecuteTransaction(transaction: (InsertClassSectionFacultyRepo) -> Unit): String? =
        try {
            if (true) {
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
    fun insertClassSectionFaculty(params: OperationParams): String? = tryExecuteTransaction { sql ->
        sql.insertClassSectionSchoolInfo(
            params.schoolName,
            params.schoolAcr,
            params.programmeName,
            params.programmeAcr,
            params.calendarTerm,
            params.calendarSection,
            params.language
        )

        params.courses.forEach { course ->
            sql.insertClassSectionFacultyInfo(
                course.name,
                course.acr,
                course.teachers.map { it.name }
            )
        }
    }
}
