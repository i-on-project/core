package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.calendarTerm.model.ExamSeason
import org.ionproject.core.calendarTerm.sql.CalendarTermData.CALENDAR_TERM
import org.ionproject.core.calendarTerm.sql.CalendarTermData.CALENDAR_TERMS_QUERY
import org.ionproject.core.calendarTerm.sql.CalendarTermData.CALENDAR_TERM_QUERY
import org.ionproject.core.calendarTerm.sql.CalendarTermData.CLASSES_QUERY
import org.ionproject.core.calendarTerm.sql.CalendarTermData.EXAM_SEASONS_QUERY
import org.ionproject.core.calendarTerm.sql.CalendarTermData.ID
import org.ionproject.core.calendarTerm.sql.CalendarTermData.LIMIT
import org.ionproject.core.calendarTerm.sql.CalendarTermData.OFFSET
import org.ionproject.core.calendarTerm.sql.CalendarTermMapper
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.klass.sql.KlassReducedMapper
import org.ionproject.core.toNullable
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.stereotype.Repository

@Repository
class CalendarTermRepoImpl(
    private val tm: TransactionManager,
    private val calendarTermMapper: CalendarTermMapper,
    private val classMapper: KlassReducedMapper
) : CalendarTermRepo {

    override fun getTerms(page: Int, limit: Int): List<CalendarTerm> {
        val result = tm.run { handle ->
            handle.createQuery(CALENDAR_TERMS_QUERY)
                .bind(OFFSET, page * limit)
                .bind(LIMIT, limit)
                .map(calendarTermMapper)
                .list()
        } as List<CalendarTerm>

        if (result.isEmpty()) {
            if (page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        return result
    }

    override fun getTermByCalId(calId: String, page: Int, limit: Int): CalendarTerm? {
        val result = tm.run { handle ->
            val term = handle.createQuery(CALENDAR_TERM_QUERY)
                .bind(ID, calId)
                .map(calendarTermMapper)
                .findOne()
                .toNullable()

            if (term != null) {
                val classes = handle.createQuery(CLASSES_QUERY)
                    .bind(ID, calId)
                    .bind(OFFSET, page * limit)
                    .bind(LIMIT, limit)
                    .map(classMapper)
                    .list()

                term.classes.addAll(classes)

                val examSeasons = handle.createQuery(EXAM_SEASONS_QUERY)
                    .bind(CALENDAR_TERM, term.calTermId)
                    .mapTo<ExamSeason>()
                    .list()

                term.examSeasons.addAll(examSeasons)
            }

            term
        }

        if (result == null) {
            if (page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        return result
    }
}
