package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.AcademicCalendar
import org.ionproject.core.ingestion.model.AcademicCalendarTerm
import org.ionproject.core.ingestion.processor.sql.CalendarIngestionDao
import org.ionproject.core.ingestion.processor.sql.model.CalendarInstant
import org.ionproject.core.ingestion.processor.sql.model.CalendarTerm
import org.ionproject.core.ingestion.processor.sql.model.CalendarTermInput
import org.jdbi.v3.sqlobject.kotlin.attach
import java.time.LocalDate
import java.time.LocalTime

class CalendarIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<AcademicCalendar> {

    override fun process(data: AcademicCalendar) {
        tm.run {
            val dao = it.attach<CalendarIngestionDao>()
            val parsedTerms = processCalendarTerms(data)
            val termList = parsedTerms.terms

            // TODO: In a worst-case scenario it will be a N+1 query - can we optimize it ???
            val latestCalendarTerm = dao.getLatestCalendarTerm()
            if (latestCalendarTerm == null) {
                // there are no terms, therefore the new terms should be added
                addNewTerms(termList, dao)
            } else {
                val latestStartDate = latestCalendarTerm.startDate.toLocalDate()
                val addList = termList.filter { parsed ->
                    val term = dao.getTermById(parsed.term)
                    if (term == null && latestStartDate < parsed.startDate) {
                        // insert new term
                        true
                    } else if (term != null) {
                        // edit term and check events
                        editTerm(parsed, term, dao)
                        false
                    } else {
                        // do nothing
                        false
                    }
                }

                if (addList.isNotEmpty())
                    addNewTerms(addList, dao)
            }
        }
    }

    private fun processCalendarTerms(data: AcademicCalendar): ParsedCalendarTerms {
        val terms = data.terms
        var latestTerm: ParsedCalendarTerm? = null
        val parsedTerms = terms.map {
            val term = it.parse()
            // it's greater or equal here because we want the latest term with the higher index
            if (latestTerm == null || term.startDate >= latestTerm!!.startDate)
                latestTerm = term

            term
        }.sortedBy { it.startDate }

        // TODO: change exception
        return ParsedCalendarTerms(
            latestTerm ?: throw Exception(),
            parsedTerms
        )
    }

    private fun addNewTerms(parsedTerms: List<ParsedCalendarTerm>, dao: CalendarIngestionDao) {
        val instantsList = parsedTerms.map { it.toCalendarInstants() }
            .flatten()

        val instantIds = dao.insertCalendarInstants(instantsList)
        val calendarTerms = parsedTerms.mapIndexed { index, parsedTerm ->
            val start = instantIds[index * 2]
            val end = instantIds[index * 2 + 1]
            parsedTerm.toCalendarTermInput(start, end)
        }

        // TODO: Add events

        dao.insertCalendarTerms(calendarTerms)
    }

    private fun editTerm(parsedTerm: ParsedCalendarTerm, term: CalendarTerm, dao: CalendarIngestionDao) {
        val instantsList = parsedTerm.toCalendarInstants()
        val instantsIds = dao.insertCalendarInstants(instantsList)

        val newTerm = parsedTerm.toCalendarTermInput(instantsIds[0], instantsIds[1])
        dao.updateCalendarTerm(newTerm)

        // TODO: Delete old instants
        // TODO: Add new events or edit already existent ones
    }
}

private data class ParsedCalendarTerms(val latest: ParsedCalendarTerm, val terms: List<ParsedCalendarTerm>)

private data class ParsedCalendarTerm(
    val term: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val data: AcademicCalendarTerm
)

private fun ParsedCalendarTerm.toCalendarTermInput(start: Int, end: Int) = CalendarTermInput(
    term,
    start,
    end
)

private fun ParsedCalendarTerm.toCalendarInstants() = listOf(
    CalendarInstant(startDate, LocalTime.MIDNIGHT),
    CalendarInstant(endDate, LocalTime.MIDNIGHT)
)

private fun AcademicCalendarTerm.parse(): ParsedCalendarTerm {
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null

    details.forEach {
        if (startDate == null || it.startDate < startDate)
            startDate = it.startDate

        if (endDate == null || it.endDate > endDate)
            endDate = it.endDate
    }

    evaluations.forEach {
        if (endDate == null || it.endDate > endDate)
            endDate = it.endDate
    }

    // TODO: change exception
    return ParsedCalendarTerm(
        calendarTerm,
        startDate ?: throw Exception(),
        endDate ?: throw Exception(),
        this
    )
}
