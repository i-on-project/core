package org.ionproject.core.programme

import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.ionproject.core.programme.sql.ProgrammeData
import org.ionproject.core.programme.sql.ProgrammeMapper
import org.ionproject.core.programme.sql.ProgrammeOfferRowReducer
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class ProgrammeRepoImpl(
    private val tm: TransactionManager,
    private val programmeMapper: ProgrammeMapper,
    private val offerRowReducer: ProgrammeOfferRowReducer
) : ProgrammeRepo {

    override fun getProgrammes(page: Int, limit: Int): List<Programme> = tm.run { handle ->
        val programmes = handle.createQuery(ProgrammeData.GET_PROGRAMMES_QUERY)
            .bind(ProgrammeData.OFFSET, page * limit)
            .bind(ProgrammeData.LIMIT, limit)
            .map(programmeMapper)
            .list()

        if (programmes.isEmpty()) {
            if (page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        programmes
    }

    override fun getProgrammeById(id: Int): Programme = tm.run { handle ->
        val programme = findProgrammeById(id, handle)
        programme
    }

    override fun getProgrammeOffers(id: Int, page: Int, limit: Int): List<ProgrammeOffer> = tm.run { handle ->
        findProgrammeById(id, handle)
        val offers = handle.createQuery(ProgrammeData.GET_PROGRAMME_OFFERS_QUERY)
            .bind(ProgrammeData.ID, id)
            .bind(ProgrammeData.OFFSET, page * limit)
            .bind(ProgrammeData.LIMIT, limit)
            .reduceRows(offerRowReducer)
            .toList()

        if (offers.isEmpty()) {
            if (page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        offers
    }

    override fun getOfferById(idProgramme: Int, idOffer: Int): ProgrammeOffer = tm.run { handle ->
        // check first if the programme exists, or else throws
        findProgrammeById(idProgramme, handle)
        handle.createQuery(ProgrammeData.GET_OFFER_DETAILS_BY_ID)
            .bind(ProgrammeData.ID, idOffer)
            .bind(ProgrammeData.PROGRAMME_ID, idProgramme)
            .reduceRows(offerRowReducer)
            .findFirst()
            .orElseThrow {
                ResourceNotFoundException("Could not find the specified offer id: \"$idOffer\" of the programme \"$idProgramme\".")
            }
    }

    private fun findProgrammeById(id: Int, handle: Handle): Programme =
        handle.createQuery(ProgrammeData.GET_PROGRAMME_BY_ID_QUERY)
            .bind(ProgrammeData.ID, id)
            .map(programmeMapper)
            .findOne()
            .orElseThrow { ResourceNotFoundException("Could not find the specified programme id: \"$id\".") }
}
