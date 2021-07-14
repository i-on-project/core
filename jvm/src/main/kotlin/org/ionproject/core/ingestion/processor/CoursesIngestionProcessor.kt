package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.SchoolCourse
import org.ionproject.core.ingestion.model.SchoolCourseProgramme
import org.ionproject.core.ingestion.model.SchoolCourses
import org.ionproject.core.ingestion.processor.sql.CourseIngestionDao
import org.ionproject.core.ingestion.processor.sql.model.RealAlternativeCourseAcronym
import org.ionproject.core.ingestion.processor.sql.model.RealCourse
import org.ionproject.core.ingestion.processor.sql.model.RealProgrammeOffer
import org.ionproject.core.ingestion.processor.sql.model.RealProgrammeOfferTerm
import org.ionproject.core.ingestion.processor.sql.model.isCourseInfoDifferent
import org.ionproject.core.ingestion.processor.sql.model.isProgrammeOfferInfoDifferent
import org.ionproject.core.ingestion.processor.sql.model.toAlternativeCourseAcronyms
import org.ionproject.core.ingestion.processor.sql.model.toRealCourse
import org.ionproject.core.ingestion.processor.util.Difference
import org.jdbi.v3.sqlobject.kotlin.attach
import org.slf4j.LoggerFactory

@FileIngestion("courses", true)
class CoursesIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<SchoolCourses> {

    companion object {
        private val log = LoggerFactory.getLogger(CoursesIngestionProcessor::class.java)
    }

    override fun process(data: SchoolCourses) {
        tm.run {
            val dao = it.attach<CourseIngestionDao>()
            val existentCourses = dao.getCourses()
            val newCourses = data.courses

            val diff = Difference(existentCourses, newCourses) { a, b ->
                a.id == b.id
            }

            if (diff.newElements.isNotEmpty())
                processCreated(diff.newElements, dao)

            if (diff.intersection.isNotEmpty())
                processUpdated(diff.intersection, dao)

            if (diff.removedElements.isNotEmpty())
                processRemoved(diff.removedElements, dao)
        }
    }

    private fun processCreated(created: Collection<SchoolCourse>, dao: CourseIngestionDao) {
        val courses = created.map {
            val real = it.toRealCourse()
            log.info("New course found: ${real.acronym} for programmes -> ${it.programmes.joinToString { p -> p.acronym }}")
            real
        }

        dao.insertCourse(courses)

        val alternativeAcronyms = created.map { it.toAlternativeCourseAcronyms(it.id) }
            .flatten()

        if (alternativeAcronyms.isNotEmpty())
            dao.insertAlternativeCourseAcronyms(alternativeAcronyms)

        val programmesMap = dao.getProgrammesMap()
        val offersTermsMap = mutableMapOf<Pair<Int, Int>, List<Int>>()

        val offers = created.map { course ->
            course.programmes.mapNotNull {
                val programmeId = programmesMap[it.acronym]?.id
                if (programmeId == null) {
                    null
                } else {
                    offersTermsMap[Pair(programmeId, course.id)] = it.termNumber
                    RealProgrammeOffer(0, programmeId, course.id, it.optional)
                }
            }
        }.flatten()

        if (offers.isNotEmpty())
            createOffersAndTerms(offers, dao) { offersTermsMap[it] }
    }

    private fun processUpdated(updated: Collection<Pair<RealCourse, SchoolCourse>>, dao: CourseIngestionDao) {
        val courseIds = updated.map { it.first.id }
        val alternativeAcronyms = dao.getAlternativeCourseAcronyms(courseIds)
        val offers = dao.getProgrammeOfferForCourse(courseIds)
        val programmesMap = dao.getProgrammesMap()

        val updateInfo = mutableListOf<RealCourse>()
        val createAcronyms = mutableListOf<RealAlternativeCourseAcronym>()
        val removeAcronyms = mutableListOf<RealAlternativeCourseAcronym>()

        val createOffers = mutableListOf<RealProgrammeOffer>()
        val updateOffers = mutableListOf<Pair<RealProgrammeOffer, SchoolCourseProgramme>>()
        val removeOffers = mutableListOf<Int>()

        val createOffersTerms = mutableMapOf<Pair<Int, Int>, List<Int>>()

        updated.forEach {
            val existent = it.first
            val new = it.second

            if (new.isCourseInfoDifferent(existent)) {
                log.info("Updating course ${existent.id} information")
                updateInfo.add(new.toRealCourse())
            }

            val acronymsList = alternativeAcronyms.filter { a -> a.courseId == existent.id }
            val acronymsDiff = Difference(acronymsList, new.acronym.drop(1)) { a, b -> a.acronym == b }

            createAcronyms.addAll(
                acronymsDiff.newElements.map { a ->
                    log.info("New acronym $a detected for course ${existent.id}")
                    RealAlternativeCourseAcronym(existent.id, a)
                }
            )

            removeAcronyms.addAll(
                acronymsDiff.removedElements.map { a ->
                    log.info("Removing acronym ${a.acronym} of course ${existent.id}")
                    a
                }
            )

            val offersList = offers.filter { o -> o.courseId == existent.id }
            val offersDiff = Difference(offersList, new.programmes) { a, b ->
                a.programmeId == programmesMap[b.acronym]?.id
            }

            createOffers.addAll(
                offersDiff.newElements.mapNotNull { p ->
                    val programme = programmesMap[p.acronym]
                    if (programme == null) {
                        null
                    } else {
                        log.info("Creating offer for course ${existent.id} in programme ${p.acronym}")
                        createOffersTerms[Pair(programme.id, existent.id)] = p.termNumber
                        RealProgrammeOffer(0, programme.id, existent.id, p.optional)
                    }
                }
            )

            updateOffers.addAll(offersDiff.intersection)

            removeOffers.addAll(
                offersDiff.removedElements.map { o ->
                    log.info("The offer ${o.id} no longer exists")
                    o.id
                }
            )
        }

        if (updateInfo.isNotEmpty())
            dao.updateCourseInformation(updateInfo)

        if (createAcronyms.isNotEmpty())
            dao.insertAlternativeCourseAcronyms(createAcronyms)

        if (removeAcronyms.isNotEmpty())
            dao.deleteAlternativeCourseAcronyms(removeAcronyms)

        if (createOffers.isNotEmpty())
            createOffersAndTerms(createOffers, dao) { createOffersTerms[it] }

        if (updateOffers.isNotEmpty())
            updateProgrammeOffers(updateOffers, dao)

        if (removeOffers.isNotEmpty())
            dao.deleteProgrammeOffer(removeOffers)
    }

    private fun createOffersAndTerms(
        offersList: List<RealProgrammeOffer>,
        dao: CourseIngestionDao,
        offersTerms: (Pair<Int, Int>) -> List<Int>?
    ) {
        val createdOffers = dao.insertProgrammeOffer(offersList)
        val newTerms = createdOffers.mapNotNull {
            offersTerms(Pair(it.programmeId, it.courseId))?.map { term ->
                RealProgrammeOfferTerm(it.id, term)
            }
        }.flatten()

        if (newTerms.isNotEmpty())
            dao.insertProgrammeOfferTerm(newTerms)
    }

    private fun updateProgrammeOffers(updated: Collection<Pair<RealProgrammeOffer, SchoolCourseProgramme>>, dao: CourseIngestionDao) {
        val offersTerms = dao.getTermsForOffer(updated.map { it.first.id })
        val updateInfo = mutableListOf<RealProgrammeOffer>()

        val createdTerms = mutableListOf<RealProgrammeOfferTerm>()
        val removedTerms = mutableListOf<RealProgrammeOfferTerm>()

        updated.forEach {
            val existent = it.first
            val new = it.second

            if (new.isProgrammeOfferInfoDifferent(existent)) {
                log.info("Updating programme offer ${existent.id} for the ${new.acronym} programme")
                updateInfo.add(RealProgrammeOffer(existent.id, existent.programmeId, existent.courseId, new.optional))
            }

            val offerTerms = offersTerms.filter { term -> term.offerId == existent.id }
            val diff = Difference(offerTerms, new.termNumber) { a, b -> a.termNumber == b }

            createdTerms.addAll(
                diff.newElements.map { term ->
                    log.info("Creating term $term for offer ${existent.id}")
                    RealProgrammeOfferTerm(existent.id, term)
                }
            )

            removedTerms.addAll(
                diff.removedElements.map { term ->
                    log.info("Removing term ${term.termNumber} for offer ${existent.id}")
                    term
                }
            )
        }

        if (createdTerms.isNotEmpty())
            dao.insertProgrammeOfferTerm(createdTerms)

        if (removedTerms.isNotEmpty())
            dao.deleteProgrammeOfferTerm(removedTerms)
    }

    private fun processRemoved(removed: Collection<RealCourse>, dao: CourseIngestionDao) {
        val offersToRemove = dao.getProgrammeOfferForCourse(
            removed.map {
                log.info("The course ${it.id} no longer exists! Removing programme offers...")
                it.id
            }
        ).map { it.id }

        dao.deleteProgrammeOffer(offersToRemove)
    }
}
