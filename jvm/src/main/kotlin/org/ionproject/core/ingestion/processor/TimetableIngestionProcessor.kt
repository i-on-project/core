package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.Timetable
import org.ionproject.core.ingestion.model.TimetableClass
import org.ionproject.core.ingestion.model.TimetableClassSection
import org.ionproject.core.ingestion.processor.sql.TimetableIngestionDao
import org.ionproject.core.ingestion.processor.sql.model.RealClass
import org.ionproject.core.ingestion.processor.sql.model.RealClassSection
import org.ionproject.core.ingestion.processor.sql.model.toEvents
import org.ionproject.core.ingestion.processor.sql.model.toRealClass
import org.ionproject.core.ingestion.processor.util.Difference
import org.jdbi.v3.sqlobject.kotlin.attach
import org.slf4j.LoggerFactory

@FileIngestion("timetable")
class TimetableIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<Timetable> {

    companion object {
        private val log = LoggerFactory.getLogger(TimetableIngestionProcessor::class.java)
    }

    override fun process(data: Timetable) {
        tm.run {
            val dao = it.attach<TimetableIngestionDao>()
            val programmeId = dao.getProgrammeIdFromAcronym(data.programme.acronym)
            if (programmeId != null) {
                val existingClasses = dao.getClassesForProgrammeAndTerm(programmeId, data.calendarTerm)
                val coursesAcronyms = if (existingClasses.isNotEmpty())
                    dao.getAcronymsForCourse(existingClasses.map { c -> c.courseId })
                else
                    emptyMap()

                val newClasses = data.classes
                val diff = Difference(existingClasses, newClasses) { a, b ->
                    coursesAcronyms[a.courseId]?.contains(b.acronym) ?: false
                }

                val info = TimetableInfo(programmeId, data.calendarTerm)
                if (diff.newElements.isNotEmpty())
                    processCreated(info, diff.newElements, dao)

                if (diff.intersection.isNotEmpty())
                    processUpdated(info, diff.intersection, dao)

                if (diff.removedElements.isNotEmpty())
                    processRemoved(diff.removedElements, dao)
            } else {
                log.info("No such programme: ${data.programme.acronym}")
            }
        }
    }

    private fun processCreated(info: TimetableInfo, classes: Collection<TimetableClass>, dao: TimetableIngestionDao) {
        val courseIds = dao.getCourseIdFromAcronym(info.programmeId, classes.map { it.acronym })
        val createList = mutableListOf<Pair<ClassInfo, Collection<TimetableClassSection>>>()

        classes.forEach {
            val courseId = courseIds[it.acronym]
            if (courseId != null) {
                log.info("Creating class ${it.acronym} (Course $courseId) in calendar term ${info.calendarTerm}")
                val classId = dao.insertClass(it.toRealClass(courseId, info.calendarTerm))
                createList.add(Pair(ClassInfo(classId, it.acronym), it.sections))
            }
        }

        if (createList.isNotEmpty())
            createClassSections(info, createList, dao)
    }

    private fun processUpdated(info: TimetableInfo, classes: Collection<Pair<RealClass, TimetableClass>>, dao: TimetableIngestionDao) {
        val createList = mutableListOf<Pair<ClassInfo, Collection<TimetableClassSection>>>()
        val updateMap = mutableMapOf<Pair<RealClassSection, TimetableClassSection>, ClassInfo>()
        val removeList = mutableListOf<RealClassSection>()

        val existentSections = dao.getClassSectionForClass(classes.map { it.first.id })
        classes.forEach { pair ->
            val existent = pair.first
            val new = pair.second

            val existentClassSections = existentSections.filter { s -> s.classId == existent.id }
            val diff = Difference(existentClassSections, new.sections) { a, b -> a.id == b.section }

            val classInfo = ClassInfo(existent.id, new.acronym)
            if (diff.newElements.isNotEmpty())
                createList.add(Pair(classInfo, diff.newElements))

            if (diff.intersection.isNotEmpty())
                diff.intersection.forEach { updateMap[it] = classInfo }

            removeList.addAll(diff.removedElements)
        }

        if (createList.isNotEmpty())
            createClassSections(info, createList, dao)

        if (updateMap.isNotEmpty())
            updateClassSections(info, updateMap, dao)

        if (removeList.isNotEmpty())
            removeClassSections(removeList, dao)
    }

    private fun createClassSections(
        timetableInfo: TimetableInfo,
        sections: Collection<Pair<ClassInfo, Collection<TimetableClassSection>>>,
        dao: TimetableIngestionDao
    ) {
        val events = sections.map {
            val classId = it.first.classId
            val classAcronym = it.first.classAcronym

            val term = dao.getCalendarTerm(timetableInfo.calendarTerm)

            it.second.forEach { k ->
                val sectionId = k.section
                log.info("Creating class section $sectionId for class $classId")
                dao.insertClassSection(RealClassSection(sectionId, classId, 0))
            }

            val realSections = dao.getClassSection(classId, it.second.map { s -> s.section })
            it.second.mapNotNull { sec ->
                val section = realSections.find { r -> r.id == sec.section }
                if (section == null)
                    null
                else
                    sec.toEvents(section.calendar, classAcronym, term.startDate, term.endDate)
            }.flatten()
        }.flatten()

        dao.insertClassSectionEvents(events)
    }

    private fun updateClassSections(
        timetableInfo: TimetableInfo,
        sections: Map<Pair<RealClassSection, TimetableClassSection>, ClassInfo>,
        dao: TimetableIngestionDao
    ) {
        // comparing two events requires a lot of SQL joins
        // therefore it may be faster to remove the old calendar components and create new ones
        val calendars = sections.keys.map { it.first.calendar }
        removeSectionCalendarComponents(calendars, dao)

        val term = dao.getCalendarTerm(timetableInfo.calendarTerm)
        val events = sections.map { (k, v) ->
            val section = k.first
            val sec = k.second

            log.info("Updating section ${sec.section} of class ${v.classId}")
            sec.toEvents(section.calendar, v.classAcronym, term.startDate, term.endDate)
        }.flatten()

        if (events.isNotEmpty())
            dao.insertClassSectionEvents(events)
    }

    private fun removeClassSections(sections: Collection<RealClassSection>, dao: TimetableIngestionDao) {
        val calendars = sections.map {
            log.info("Removing calendar of section ${it.id} of class ${it.classId}")
            it.calendar
        }

        removeSectionCalendarComponents(calendars, dao)
        dao.deleteCalendar(calendars)

        dao.deleteClassSection(
            sections.map {
                log.info("Removing section ${it.id} of class ${it.classId}")
                it.id
            }
        )
    }

    private fun removeSectionCalendarComponents(calendars: List<Int>, dao: TimetableIngestionDao) {
        val calendarComponents = dao.getCalendarComponents(calendars)
        dao.deleteCalendarComponent(calendarComponents)
    }

    private fun processRemoved(classes: Collection<RealClass>, dao: TimetableIngestionDao) {
        val classIds = classes.map {
            log.info("Removing class ${it.id}")
            it.id
        }

        dao.deleteClass(classIds)
    }
}

private data class TimetableInfo(
    val programmeId: Int,
    val calendarTerm: String
)

private data class ClassInfo(
    val classId: Int,
    val classAcronym: String
)
