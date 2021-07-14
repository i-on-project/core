package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.SchoolProgramme
import org.ionproject.core.ingestion.model.SchoolProgrammes
import org.ionproject.core.ingestion.processor.sql.ProgrammeIngestionDao
import org.ionproject.core.ingestion.processor.sql.model.RealProgrammeCoordinator
import org.ionproject.core.ingestion.processor.sql.model.RealSchoolProgramme
import org.ionproject.core.ingestion.processor.sql.model.isProgrammeInfoDifferent
import org.ionproject.core.ingestion.processor.sql.model.toCoordinators
import org.ionproject.core.ingestion.processor.sql.model.toRealProgramme
import org.ionproject.core.ingestion.processor.util.Difference
import org.jdbi.v3.sqlobject.kotlin.attach
import org.slf4j.LoggerFactory

@FileIngestion("school_programmes", true)
class ProgrammesIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<SchoolProgrammes> {

    companion object {
        private val log = LoggerFactory.getLogger(ProgrammesIngestionProcessor::class.java)
    }

    override fun process(data: SchoolProgrammes) {
        tm.run {
            val dao = it.attach<ProgrammeIngestionDao>()
            val existentProgrammes = dao.getProgrammes()
            val newProgrammes = data.programmes

            val diff = Difference(existentProgrammes, newProgrammes) { a, b ->
                a.acronym == b.acronym
            }

            if (diff.newElements.isNotEmpty())
                processCreated(diff.newElements, dao)

            if (diff.intersection.isNotEmpty())
                processUpdated(diff.intersection, dao)

            if (diff.removedElements.isNotEmpty())
                processRemoved(diff.removedElements, dao)
        }
    }

    private fun processCreated(created: Collection<SchoolProgramme>, dao: ProgrammeIngestionDao) {
        val programmes = created.map {
            log.info("Creating new programme: ${it.acronym}")
            it.toRealProgramme()
        }

        val generatedKeys = dao.insertProgrammes(programmes)
        val coordinators = created.mapIndexed { i, p ->
            log.info("Creating ${p.acronym} programme coordinators")
            p.toCoordinators(generatedKeys[i].id)
        }.flatten()

        if (coordinators.isNotEmpty())
            dao.insertProgrammeCoordinators(coordinators)
    }

    private fun processUpdated(updated: Collection<Pair<RealSchoolProgramme, SchoolProgramme>>, dao: ProgrammeIngestionDao) {
        val coordinators = dao.getProgrammesCoordinators(updated.map { it.first.id })

        val setAsAvailable = mutableListOf<Int>()
        val infoChanged = mutableListOf<RealSchoolProgramme>()

        val createdCoordinators = mutableListOf<RealProgrammeCoordinator>()
        val removedCoordinators = mutableListOf<Int>()

        updated.forEach {
            val existent = it.first
            val new = it.second

            if (!existent.available) {
                log.info("Programme ${existent.acronym} was unavailable. Setting as available now")
                setAsAvailable.add(existent.id)
            }

            if (new.isProgrammeInfoDifferent(existent)) {
                log.info("The information for the ${existent.acronym} programme has changed")
                infoChanged.add(new.toRealProgramme(existent.id))
            }

            val programmeCoordinators = coordinators.filter { i -> i.programmeId == existent.id }
            val coordinatorsDiff = Difference(programmeCoordinators, new.coordination) { a, b ->
                a.name == b
            }

            createdCoordinators.addAll(
                coordinatorsDiff.newElements.map { name ->
                    log.info("New ${existent.acronym} programme coordinator: $name")
                    RealProgrammeCoordinator(programmeId = existent.id, name = name)
                }
            )

            removedCoordinators.addAll(
                coordinatorsDiff.removedElements.map { coordinator ->
                    log.info("Removed ${existent.acronym} programme coordinator: ${coordinator.name}")
                    coordinator.id
                }
            )
        }

        if (setAsAvailable.isNotEmpty())
            dao.setProgrammeAvailable(setAsAvailable, true)

        if (infoChanged.isNotEmpty())
            dao.updateProgrammeInformation(infoChanged)

        if (createdCoordinators.isNotEmpty())
            dao.insertProgrammeCoordinators(createdCoordinators)

        if (removedCoordinators.isNotEmpty())
            dao.deleteProgrammeCoordinators(removedCoordinators)
    }

    private fun processRemoved(removed: Collection<RealSchoolProgramme>, dao: ProgrammeIngestionDao) {
        val ids = removed.map {
            log.info("Setting ${it.acronym} programme as unavailable")
            it.id
        }

        dao.setProgrammeAvailable(ids, false)
    }
}
