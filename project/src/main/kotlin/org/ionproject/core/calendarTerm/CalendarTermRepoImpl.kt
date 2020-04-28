package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.klass.mappers.KlassReducedMapper
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

@Repository
class CalendarTermRepoImpl(private val tm: TransactionManager,
                           private val calendarTermMapper: CalendarTermMapper,
                           private val classMapper: KlassReducedMapper) : CalendarTermRepo {

    override fun getTerms(page: Int, limit: Int): List<CalendarTerm> {
        val result = tm.run {
            handle -> {
                    handle.createQuery("SELECT * FROM dbo.CalendarTerm OFFSET :offset LIMIT :lim")
                            .bind("offset", page * limit)
                            .bind("lim", limit)
                            .map(calendarTermMapper)
                            .list()
            }()
        }

        if (result?.size == 0) {
            if (page == 0)
                return listOf()      //Case has 0 results, normal view with empty list
            else
                throw ResourceNotFoundException("There is no calendar-term at page $page with limit $limit.")
            //User requested a resource specifing a page that has no results. e.g. /v0/courses?page=5&limit=100
        } else
            return result as List<CalendarTerm>  //Either had default values or not, there was a result...
    }

    override fun getTermByCalId(calId: String, page: Int, limit: Int): CalendarTerm {
        val result = tm.run {
            handle ->
            {
                val res = handle.createQuery("SELECT * FROM dbo.CalendarTerm WHERE id=:id")
                        .bind("id", calId)
                        .map(calendarTermMapper)
                        .findOne()

                var term : CalendarTerm? = null
                if(res.isPresent) {
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
            }()
        }

        return result ?: throw ResourceNotFoundException("Term with id=$calId was not found.")
    }
}