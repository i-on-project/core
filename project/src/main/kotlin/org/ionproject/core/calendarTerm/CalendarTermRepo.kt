package org.ionproject.core.calendarTerm

import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.mappers.CalendarTermMapper
import org.ionproject.core.common.mappers.ClassMapper
import org.ionproject.core.common.model.CalendarTerm
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.klass.Klass
import org.jdbi.v3.core.statement.Query
import org.springframework.stereotype.Repository

@Repository
class CalendarTermRepo(private val tm: TransactionManager) {
    val calendarTermMapper: CalendarTermMapper = CalendarTermMapper()
    val classMapper: ClassMapper = ClassMapper()

    fun getTerms(page: Int, limit: Int, defaultFlag: Boolean): List<CalendarTerm> {
        val result = tm.run {
            handle -> {

                val q: Query
                if(defaultFlag) {
                    q = handle.createQuery("SELECT * FROM dbo.CalendarTerm")
                } else {
                    q = handle.createQuery("SELECT * FROM dbo.CalendarTerm OFFSET :offset LIMIT :lim")
                            .bind("offset", page * limit)
                            .bind("lim", limit)
                }
                q.map(calendarTermMapper)
                        .list()
            }()
        }

        if (result?.size == 0) {
            if (defaultFlag)
                return listOf()      //Case has 0 results, but user didn't use params. then normal view with empty list
            else
                throw ResourceNotFoundException("There is no course at the page $page with limit $limit.")
            //User requested a resource specifing query parameters and such held no result. e.g. /v0/courses?page=5&limit=100
        } else
            return result as List<CalendarTerm>  //Either had default values or not, there was a result...
    }

    fun getTermByCalId(calId: String, page: Int, limit: Int, defaultFlag: Boolean): CalendarTerm {
        val result = tm.run {
            handle ->
            {
                val res = handle.createQuery("SELECT * FROM dbo.CalendarTerm WHERE id=:id")
                        .bind("id", calId)
                        .map(calendarTermMapper)
                        .findOne()

                val term = res?.get() ?: throw ResourceNotFoundException("Term with id=$calId was not found.")

                val q: Query
                if(defaultFlag) {
                    q = handle.createQuery("SELECT * FROM dbo.Class WHERE term=:id")
                            .bind("id", calId)
                } else {
                    q = handle.createQuery("SELECT * FROM dbo.Class WHERE term=:id OFFSET :offset LIMIT :lim")
                            .bind("id", calId)
                            .bind("offset", page * limit)
                            .bind("lim", limit)
                }

                val classes = q.map(classMapper).list()

                term.classes.addAll(classes)
                term
            }()
        }

        if (result != null)
            return result
        else
            throw InternalServerErrorException("Something weird happened after successfully reading term and while reading classes... Try again later.")

    }
}