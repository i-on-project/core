package org.ionproject.core.ingestion.processor.sql

import org.ionproject.core.ingestion.processor.sql.model.RealCalendarTerm
import org.ionproject.core.ingestion.processor.sql.model.RealClass
import org.ionproject.core.ingestion.processor.sql.model.RealClassSection
import org.ionproject.core.ingestion.processor.sql.model.RealEventWithDateReferences
import org.jdbi.v3.sqlobject.config.KeyColumn
import org.jdbi.v3.sqlobject.config.ValueColumn
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface TimetableIngestionDao {

    @SqlQuery(
        """
        select id from dbo.Programme
        where acronym = :acronym
    """
    )
    fun getProgrammeIdFromAcronym(acronym: String): Int?

    @SqlQuery(
        """
        select cl.* from dbo.Class cl 
        join dbo.ProgrammeOffer po on cl.courseId = po.courseId
        where po.programmeId = :programmeId and cl.calendarTerm = :calendarTerm
    """
    )
    fun getClassesForProgrammeAndTerm(programmeId: Int, calendarTerm: String): List<RealClass>

    @SqlQuery(
        """
        select co.id, array_agg(distinct co.acronym) || array_agg(distinct alt.acronym) filter (where alt.acronym is not null) as acronyms
        from dbo.Course co
        left join dbo.AlternativeCourseAcronyms alt on alt.courseId = co.id
        where co.id in (<course>)
        group by co.id
    """
    )
    @KeyColumn("id")
    @ValueColumn("acronyms")
    fun getAcronymsForCourse(@BindList("course") course: List<Int>): Map<Int, Set<String>>

    @SqlQuery(
        """
        select co.id, co.acronym
        from dbo.ProgrammeOffer po
        join (
            select id, acronym from dbo.Course
            union (select * from dbo.AlternativeCourseAcronyms)
        ) co on co.id = po.courseId
        where po.programmeId = :programmeId and co.acronym in (<acronym>)
    """
    )
    @KeyColumn("acronym")
    @ValueColumn("id")
    fun getCourseIdFromAcronym(programmeId: Int, @BindList("acronym") acronym: List<String>): Map<String, Int>

    @SqlQuery(
        """
        select dbo.f_classCalendarCreate(:klass.calendarTerm, :klass.courseId)
    """
    )
    fun insertClass(klass: RealClass): Int

    @SqlBatch(
        """
        delete from dbo.Class where id = :classId
    """
    )
    fun deleteClass(classId: List<Int>)

    @SqlQuery(
        """
        select dbo.f_classSectionCalendarCreate(:section.classId, :section.id)
    """
    )
    fun insertClassSection(section: RealClassSection): String

    @SqlQuery(
        """
        select * from dbo.ClassSection where classId = :classId and id in (<sectionId>)
    """
    )
    fun getClassSection(classId: Int, @BindList("sectionId") sectionId: List<String>): List<RealClassSection>

    @SqlQuery(
        """
        select * from dbo.ClassSection where classId in (<classId>)
    """
    )
    fun getClassSectionForClass(@BindList("classId") classId: List<Int>): List<RealClassSection>

    @SqlQuery(
        """
        select comp_id from dbo.CalendarComponents where calendar_id in (<calendarId>)
    """
    )
    fun getCalendarComponents(@BindList("calendarId") calendarId: List<Int>): List<Int>

    @SqlBatch(
        """
        delete from dbo.CalendarComponent where id = :componentId
    """
    )
    fun deleteCalendarComponent(componentId: List<Int>)

    @SqlBatch(
        """
        delete from dbo.Calendar where id = :calendarId
    """
    )
    fun deleteCalendar(calendarId: List<Int>)

    @SqlBatch(
        """
        delete from dbo.ClassSection where id = :sectionId
    """
    )
    fun deleteClassSection(sectionId: List<String>)

    @SqlQuery(
        """
        select * from dbo._CalendarTerm where id = :calendarTerm
    """
    )
    fun getCalendarTerm(calendarTerm: String): RealCalendarTerm

    @SqlBatch(
        """
        call dbo.newEventWithDateReferences(
            :event.calendarId,
            :event.summary,
            :event.summaryLanguage,
            :event.description,
            :event.descriptionLanguage,
            :event.category,
            :event.startInstant,
            :event.startTime,
            :event.endTime,
            :event.dateType,
            :event.location,
            :event.weekday,
            :event.until
        )
    """
    )
    fun insertClassSectionEvents(event: List<RealEventWithDateReferences>)
}
