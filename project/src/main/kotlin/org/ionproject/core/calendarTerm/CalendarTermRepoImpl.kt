package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.klass.mappers.KlassReducedMapper
import org.springframework.stereotype.Repository

@Repository
class CalendarTermRepoImpl(
    private val tm: TransactionManager,
    private val calendarTermMapper: CalendarTermMapper,
    private val classMapper: KlassReducedMapper
) : CalendarTermRepo {

    override fun getTerms(page: Int, limit: Int): List<CalendarTerm> {
        val result = tm.run { handle ->
            handle.createQuery("SELECT * FROM dbo.CalendarTerm OFFSET :offset LIMIT :lim")
                    .bind("offset", page * limit)
                    .bind("lim", limit)
                    .map(calendarTermMapper)
                    .list()
        } as List<CalendarTerm>

        if(result.isEmpty())
            throw ResourceNotFoundException("No calendar-terms found. (Check if your page & limit are valid)")
        return result
    }

    override fun getTermByCalId(calId: String, page: Int, limit: Int): CalendarTerm? = tm.run { handle ->
        val res = handle.createQuery("SELECT * FROM dbo.CalendarTerm WHERE id=:id")
            .bind("id", calId)
            .map(calendarTermMapper)
            .findOne()

        var term: CalendarTerm? = null
        if (res.isPresent) {
            term = res.get()

            val classes = handle.createQuery("SELECT * FROM dbo.Class WHERE term=:id OFFSET :offset LIMIT :lim")
                .bind("id", calId)
                .bind("offset", page * limit)
                .bind("lim", limit)
                .map(classMapper)
                .list()

            term.classes.addAll(classes)
        }
        term
    }
}