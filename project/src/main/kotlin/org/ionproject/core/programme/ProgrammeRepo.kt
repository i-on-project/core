package org.ionproject.core.programme

import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.mappers.ProgrammeMapper
import org.ionproject.core.common.mappers.ProgrammeOfferMapper
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Component

@Component
class ProgrammeRepo(private val tm: TransactionManager) {
    val programmeMapper: ProgrammeMapper = ProgrammeMapper()
    val offerMapper: ProgrammeOfferMapper = ProgrammeOfferMapper()

    fun getProgrammes(): List<Programme> {
        val result = tm.run { handle ->
            handle.createQuery("SELECT * FROM dbo.programme")
                .map(programmeMapper)
                .list()
        }

        return result ?: listOf()
    }

    fun getProgrammeById(id: Int): Programme {
        val result = tm.run { handle ->
            {
                val res = handle.createQuery("SELECT * FROM dbo.programme WHERE id= :id")
                    .bind("id", id)
                    .map(programmeMapper)
                    .findOne()

                val programme = res?.get() ?: throw ResourceNotFoundException("Project with id=$id was not found.")

                val offers = handle.createQuery(
                    """ SELECT po.*,co.acronym AS courseAcr FROM dbo.programmeOffer AS po INNER JOIN dbo.course AS co
                        ON po.courseId=co.id
                        WHERE programmeId = :id 
                        """.trimIndent()
                )
                    .bind("id", id)
                    .map(offerMapper)
                    .list()

                programme.offers.addAll(offers)
                programme
            }()
        }

        if (result != null)
            return result
        else
            throw InternalServerErrorException("Something weird happened after successfully reading project and while reading offers... Try again later.")
    }

    fun getOfferById(id: Int): ProgrammeOffer {
        val result = tm.run { handle ->
            handle.createQuery("SELECT * FROM dbo.programmeOffer WHERE id = :id")
                .bind("id", id)
                .map(offerMapper)
                .findOne()
        }

        if (result?.isPresent == true)
            return result.get()
        else
            throw ResourceNotFoundException("Offer by id:$id was not found.")
    }
}