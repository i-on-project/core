package org.ionproject.core.ingestion.processor.sql

import org.ionproject.core.ingestion.processor.sql.model.CalendarInstant
import org.ionproject.core.ingestion.processor.sql.model.CalendarTerm
import org.ionproject.core.ingestion.processor.sql.model.CalendarTermInput
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface CalendarIngestionDao {

    @SqlQuery(
        """
        select id, start_date, end_date from dbo.CalendarTerm 
        order by start_date desc
        limit 1
    """
    )
    fun getLatestCalendarTerm(): CalendarTerm?

    @SqlQuery(
        """
        select id, start_date, end_date from dbo.CalendarTerm
        where id = :id
    """
    )
    fun getTermById(id: String): CalendarTerm?

    @SqlBatch(
        """
        insert into dbo.Instant (date, time)
        values (:instant.date, :instant.time)
    """
    )
    @GetGeneratedKeys
    fun insertCalendarInstants(@BindBean("instant") instants: List<CalendarInstant>): List<Int>

    @SqlBatch(
        """
        insert into dbo._CalendarTerm (id, start_date, end_date)
        values (:term.id, :term.startDate, :term.endDate)
    """
    )
    fun insertCalendarTerms(@BindBean("term") terms: List<CalendarTermInput>)

    @SqlUpdate(
        """
        update dbo._CalendarTerm
        set start_date = :term.startDate, end_date = :term.endDate
        where id = :term.id
    """
    )
    fun updateCalendarTerm(term: CalendarTermInput)

    @SqlUpdate(
        """
        delete from dbo.Instant
        where id in (<instants>)
    """
    )
    fun deleteInstants(instants: List<Int>)
}
