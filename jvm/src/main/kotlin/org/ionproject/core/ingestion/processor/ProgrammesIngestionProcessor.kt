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

            val created = newProgrammes.filter { i -> existentProgrammes.none { k -> i.acronym == k.acronym } }

            val updated = existentProgrammes.mapNotNull { i ->
                val programme = newProgrammes.find { k -> i.acronym == k.acronym }
                if (programme == null)
                    null
                else
                    Pair(i, programme)
            }

            val removed = existentProgrammes.filter { i -> newProgrammes.none { k -> i.acronym == k.acronym } }

            if (created.isNotEmpty())
                processCreated(created, dao)

            if (updated.isNotEmpty())
                processUpdated(updated, dao)

            if (removed.isNotEmpty())
                processRemoved(removed, dao)
        }
    }

    private fun processCreated(created: List<SchoolProgramme>, dao: ProgrammeIngestionDao) {
        val programmes = created.map {
            log.info("Creating new programme: ${it.acronym}")
            it.toRealProgramme()
        }

        val generatedKeys = dao.insertProgrammes(programmes)
        val coordinators = created.mapIndexed { i, p ->
            log.info("Creating ${p.acronym} programme coordinators")
            p.toCoordinators(generatedKeys[i])
        }.flatten()

        if (coordinators.isNotEmpty())
            dao.insertProgrammeCoordinators(coordinators)
    }

    private fun processUpdated(updated: List<Pair<RealSchoolProgramme, SchoolProgramme>>, dao: ProgrammeIngestionDao) {
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
            createdCoordinators.addAll(
                new.coordination.filter { i -> programmeCoordinators.none { k -> i == k.name } }
                    .map { name ->
                        log.info("New ${existent.acronym} programme coordinator: $name")
                        RealProgrammeCoordinator(programmeId = existent.id, name = name)
                    }
            )

            removedCoordinators.addAll(
                programmeCoordinators.filter { i -> new.coordination.none { k -> i.name == k } }
                    .map { i ->
                        log.info("Removed ${existent.acronym} programme coordinator: ${i.name}")
                        i.id
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

    private fun processRemoved(removed: List<RealSchoolProgramme>, dao: ProgrammeIngestionDao) {
        val ids = removed.map {
            log.info("Setting ${it.acronym} programme as unavailable")
            it.id
        }

        dao.setProgrammeAvailable(ids, false)
    }

}