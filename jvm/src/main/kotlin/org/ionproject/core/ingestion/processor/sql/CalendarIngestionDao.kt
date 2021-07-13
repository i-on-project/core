package org.ionproject.core.ingestion.processor.sql

import org.ionproject.core.calendarTerm.model.ExamSeason
import org.ionproject.core.ingestion.processor.sql.model.CalendarInstant
import org.ionproject.core.ingestion.processor.sql.model.CalendarTerm
import org.ionproject.core.ingestion.processor.sql.model.ExamSeasonInput
import org.ionproject.core.ingestion.processor.sql.model.RealCalendarTerm
import org.jdbi.v3.sqlobject.config.KeyColumn
import org.jdbi.v3.sqlobject.customizer.BindList
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

    @SqlQuery(
        """
            select id, start_date, end_date from dbo.CalendarTerm
            where id in (<ids>)
        """
    )
    @KeyColumn("id")
    fun getTermsByIds(@BindList("ids") ids: List<String>): Map<String, CalendarTerm>

    @SqlQuery(
        """
            select id, start_date, end_date from dbo._CalendarTerm
            where id = :id
        """
    )
    fun getRealTermById(id: String): RealCalendarTerm?

    @SqlBatch(
        """
        insert into dbo.Instant (date, time)
        values (:instant.date, :instant.time)
    """
    )
    @GetGeneratedKeys
    fun insertCalendarInstants(instant: List<CalendarInstant>): List<Int>

    @SqlBatch(
        """
        insert into dbo._CalendarTerm (id, start_date, end_date)
        values (:term.id, :term.startDate, :term.endDate)
    """
    )
    fun insertCalendarTerms(term: List<RealCalendarTerm>)

    @SqlUpdate(
        """
        update dbo._CalendarTerm
        set start_date = :term.startDate, end_date = :term.endDate
        where id = :term.id
    """
    )
    fun updateCalendarTerm(term: RealCalendarTerm)

    @SqlUpdate(
        """
        delete from dbo.Instant
        where id in (<instants>)
    """
    )
    fun deleteInstants(instants: List<Int>)

    @SqlBatch(
        """
            insert into dbo._ExamSeason (calendarTerm, description, startDate, endDate)
            values (:season.calendarTerm, :season.description, :season.startDate, :season.endDate)
        """
    )
    fun insertExamSeasons(season: List<ExamSeasonInput>)

    @SqlQuery(
        """
            select * from dbo.ExamSeason
            where calendarTerm = :term
        """
    )
    fun getExamSeasonsForTerm(term: String): List<ExamSeason>

    @SqlQuery(
        """
            select * from dbo._ExamSeason
            where id = :id
        """
    )
    fun getRealExamSeasonById(id: Int): ExamSeasonInput

    @SqlUpdate(
        """
            update dbo._ExamSeason
            set startDate = :season.startDate, endDate = :season.endDate
            where id = :season.id
        """
    )
    fun updateExamSeason(season: ExamSeasonInput)
}
