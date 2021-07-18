package org.ionproject.core.ingestion.processor

import org.ionproject.core.calendarTerm.model.ExamSeason
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.AcademicCalendar
import org.ionproject.core.ingestion.model.AcademicCalendarTerm
import org.ionproject.core.ingestion.processor.sql.CalendarIngestionDao
import org.ionproject.core.ingestion.processor.sql.model.CalendarInstant
import org.ionproject.core.ingestion.processor.sql.model.CalendarTerm
import org.ionproject.core.ingestion.processor.sql.model.RealCalendarTerm
import org.ionproject.core.ingestion.processor.sql.model.toCalendarInstants
import org.ionproject.core.ingestion.processor.sql.model.toExamSeasonInput
import org.ionproject.core.ingestion.processor.util.Difference
import org.jdbi.v3.sqlobject.kotlin.attach
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@FileIngestion("calendar", true)
class CalendarIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<AcademicCalendar> {

    companion object {
        private val log = LoggerFactory.getLogger(CalendarIngestionProcessor::class.java)
    }

    override fun process(data: AcademicCalendar) {
        tm.run {
            val dao = it.attach<CalendarIngestionDao>()
            val parsedTerms = processCalendarTerms(data)
            val existentTerms = dao.getCalendarTerms()

            val diff = Difference(existentTerms, parsedTerms) { a, b -> a.id == b.term }

            if (diff.newElements.isNotEmpty())
                createCalendarTerms(diff.newElements, dao)

            if (diff.intersection.isNotEmpty())
                updateCalendarTerms(diff.intersection, dao)
        }
    }

    private fun processCalendarTerms(data: AcademicCalendar): List<ParsedCalendarTerm> {
        val terms = data.terms
        return terms.map { it.parse() }
    }

    private fun createCalendarTerms(parsedTerms: Collection<ParsedCalendarTerm>, dao: CalendarIngestionDao) {
        val instantsList = parsedTerms.map { it.toCalendarInstants() }
            .flatten()

        val instantIds = dao.insertCalendarInstants(instantsList)
        val calendarTerms = parsedTerms.mapIndexed { index, parsedTerm ->
            val start = instantIds[index * 2]
            val end = instantIds[index * 2 + 1]

            log.info("Creating calendar term ${parsedTerm.term}")
            parsedTerm.toCalendarTermInput(start, end)
        }

        dao.insertCalendarTerms(calendarTerms)

        val examSeasons = parsedTerms.map {
            it.data.evaluations
                .map { ev ->
                    ExamSeason(
                        calendarTerm = it.term,
                        description = ev.name,
                        startDate = ev.startDate,
                        endDate = ev.endDate
                    )
                }
        }.flatten()

        if (examSeasons.isNotEmpty())
            createExamSeasons(examSeasons, dao)
    }

    private fun createExamSeasons(examSeasons: List<ExamSeason>, dao: CalendarIngestionDao) {
        val instants = examSeasons.map { it.toCalendarInstants() }.flatten()
        val instantIds = dao.insertCalendarInstants(instants)

        val mappedSeasons = examSeasons.mapIndexed { index, input ->
            val start = instantIds[index * 2]
            val end = instantIds[index * 2 + 1]
            input.toExamSeasonInput(start, end)
        }

        dao.insertExamSeasons(mappedSeasons)
    }

    private fun updateCalendarTerms(terms: Collection<Pair<CalendarTerm, ParsedCalendarTerm>>, dao: CalendarIngestionDao) {
        terms.forEach { t ->
            val k = t.second
            val v = t.first

            log.info("Updating calendar term ${k.term}")
            updateCalendarTermsDates(k, v, dao)
            updateCalendarTermsExamSeasons(k, v, dao)
        }
    }

    private fun updateCalendarTermsDates(parsedTerm: ParsedCalendarTerm, term: CalendarTerm, dao: CalendarIngestionDao) {
        val start = LocalDateTime.of(parsedTerm.startDate, LocalTime.MIDNIGHT)
        val end = LocalDateTime.of(parsedTerm.endDate, LocalTime.MIDNIGHT)
        if (start == term.startDate && end == term.endDate)
            return

        val oldTerm = dao.getRealTermById(term.id)!!

        val instantsList = parsedTerm.toCalendarInstants()
        val instantsIds = dao.insertCalendarInstants(instantsList)

        val newTerm = parsedTerm.toCalendarTermInput(instantsIds[0], instantsIds[1])

        dao.updateCalendarTerm(newTerm)
        dao.deleteInstants(listOf(oldTerm.startDate, oldTerm.endDate))
    }

    private fun updateCalendarTermsExamSeasons(parsedTerm: ParsedCalendarTerm, term: CalendarTerm, dao: CalendarIngestionDao) {
        val existingEvaluations = dao.getExamSeasonsForTerm(term.id)
        val evaluations = parsedTerm.data.evaluations

        val evaluationsMap = mutableMapOf<String, ExamSeason>()
        existingEvaluations.forEach { evaluationsMap[it.description] = it }

        val toCreate = mutableListOf<ExamSeason>()
        val toUpdate = mutableListOf<ExamSeason>()

        evaluations.forEach {
            val ev = evaluationsMap[it.name]
            if (ev == null) {
                log.info("Creating new exam season: ${term.id} - ${it.name}")
                toCreate.add(
                    ExamSeason(
                        calendarTerm = term.id,
                        description = it.name,
                        startDate = it.startDate,
                        endDate = it.endDate
                    )
                )
            } else if (it.startDate != ev.startDate || it.endDate != ev.endDate) {
                log.info("Updating exam season: ${term.id} - ${it.name}")
                toUpdate.add(
                    ExamSeason(
                        ev.id,
                        ev.calendarTerm,
                        ev.description,
                        it.startDate,
                        it.endDate
                    )
                )
            }
        }

        if (toCreate.isNotEmpty())
            createExamSeasons(toCreate, dao)

        toUpdate.forEach { season ->
            val realSeason = dao.getRealExamSeasonById(season.id)

            val newInstants = dao.insertCalendarInstants(season.toCalendarInstants())
            dao.updateExamSeason(season.toExamSeasonInput(newInstants[0], newInstants[1]))

            dao.deleteInstants(listOf(realSeason.startDate, realSeason.endDate))
        }
    }
}

private data class ParsedCalendarTerm(
    val term: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val data: AcademicCalendarTerm
)

private fun ParsedCalendarTerm.toCalendarTermInput(start: Int, end: Int) = RealCalendarTerm(
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

    lectures.forEach {
        if (startDate == null || it.startDate < startDate)
            startDate = it.startDate

        if (endDate == null || it.endDate > endDate)
            endDate = it.endDate
    }

    return ParsedCalendarTerm(
        calendarTerm,
        startDate ?: throw Exception("Start date not found for calendar term"),
        endDate ?: throw Exception("End date not found for calendar term"),
        this
    )
}
