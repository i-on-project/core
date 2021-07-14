package org.ionproject.core.ingestion.processor.sql

import org.ionproject.core.ingestion.processor.sql.model.RealAlternativeCourseAcronym
import org.ionproject.core.ingestion.processor.sql.model.RealCourse
import org.ionproject.core.ingestion.processor.sql.model.RealProgrammeOffer
import org.ionproject.core.ingestion.processor.sql.model.RealProgrammeOfferTerm
import org.ionproject.core.ingestion.processor.sql.model.RealSchoolProgramme
import org.jdbi.v3.sqlobject.config.KeyColumn
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface CourseIngestionDao {

    @SqlQuery(
        """
        select * from dbo.Programme
    """
    )
    @KeyColumn("acronym")
    fun getProgrammesMap(): Map<String, RealSchoolProgramme>

    @SqlBatch(
        """
        insert into dbo.Course (id, acronym, name, credits, scientificArea, termDuration)
        values (:c.id, :c.acronym, :c.name, :c.credits, :c.scientificArea, :c.termDuration)
    """
    )
    @GetGeneratedKeys
    fun insertCourse(c: List<RealCourse>): List<RealCourse>

    @SqlQuery(
        """
        select * from dbo.Course
    """
    )
    fun getCourses(): List<RealCourse>

    @SqlBatch(
        """
        update dbo.Course
        set acronym = :c.acronym, name = :c.name, credits = :c.credits, scientificArea = :c.scientificArea, termDuration = :c.termDuration
        where id = :c.id
    """
    )
    fun updateCourseInformation(c: List<RealCourse>)

    @SqlBatch(
        """
        insert into dbo.AlternativeCourseAcronyms (courseId, acronym)
        values (:alt.courseId, :alt.acronym)
    """
    )
    fun insertAlternativeCourseAcronyms(alt: List<RealAlternativeCourseAcronym>)

    @SqlQuery(
        """
        select * from dbo.AlternativeCourseAcronyms
        where courseId in (<courses>)
    """
    )
    fun getAlternativeCourseAcronyms(@BindList("courses") courses: List<Int>): List<RealAlternativeCourseAcronym>

    @SqlBatch(
        """
        delete from dbo.AlternativeCourseAcronyms
        where courseId = :alt.courseId and acronym = :alt.acronym
    """
    )
    fun deleteAlternativeCourseAcronyms(alt: List<RealAlternativeCourseAcronym>)

    @SqlBatch(
        """
        insert into dbo.ProgrammeOffer (programmeId, courseId, optional)
        values (:offer.programmeId, :offer.courseId, :offer.optional)
    """
    )
    @GetGeneratedKeys
    fun insertProgrammeOffer(offer: List<RealProgrammeOffer>): List<RealProgrammeOffer>

    @SqlQuery(
        """
        select * from dbo.ProgrammeOffer
        where courseId in (<courses>)
    """
    )
    fun getProgrammeOfferForCourse(@BindList("courses") courses: List<Int>): List<RealProgrammeOffer>

    @SqlBatch(
        """
        delete from dbo.ProgrammeOffer
        where id = :offer
    """
    )
    fun deleteProgrammeOffer(offer: List<Int>)

    @SqlBatch(
        """
        insert into dbo.ProgrammeOfferTerm (offerId, termNumber)
        values (:offerTerm.offerId, :offerTerm.termNumber)
    """
    )
    fun insertProgrammeOfferTerm(offerTerm: List<RealProgrammeOfferTerm>)

    @SqlQuery(
        """
        select * from dbo.ProgrammeOfferTerm
        where offerId in (<offer>)
    """
    )
    fun getTermsForOffer(@BindList("offer") offer: List<Int>): List<RealProgrammeOfferTerm>

    @SqlBatch(
        """
        delete from dbo.ProgrammeOfferTerm
        where offerId = :offerTerm.offerId and termNumber = :offerTerm.termNumber
    """
    )
    fun deleteProgrammeOfferTerm(offerTerm: List<RealProgrammeOfferTerm>)
}
