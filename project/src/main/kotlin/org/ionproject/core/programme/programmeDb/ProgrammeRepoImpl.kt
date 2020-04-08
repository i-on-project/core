package org.ionproject.core.programme.programmeDb

import org.ionproject.core.common.mappers.ProgrammeMapper
import org.ionproject.core.common.mappers.ProgrammeOfferMapper
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Component

@Component
class ProgrammeRepoImpl(private val tm : TransactionManager) : ProgrammeRepo {
    val programmeMapper : ProgrammeMapper = ProgrammeMapper()
    val offerMapper : ProgrammeOfferMapper = ProgrammeOfferMapper()

    override fun getProgrammes(): List<Programme> {
        val result = tm.run {
            handle -> handle.createQuery("SELECT * FROM dbo.programme")
                .map(programmeMapper)
                .list()
        }

        return result ?: listOf()
     }

    override fun getProgrammeById(id: Int): Programme? {
        val result = tm.run {
            handle ->
            {
                val programme = handle.createQuery("SELECT * FROM dbo.programme WHERE id= :id")
                        .bind("id", id)
                        .map(programmeMapper)
                        .one()

                val offers = handle.createQuery(
                        """
                                SELECT po.*,co.acronym AS courseAcr FROM dbo.programmeOffer AS po INNER JOIN dbo.course AS co
                                ON po.courseId=co.id
                                WHERE programmeId = :id 
                            """
                )
                        .bind("id", id)
                        .map(offerMapper)
                        .list()

                programme.offers.addAll(offers)
                programme
            }()
        }

        return result
    }

    override fun getOfferById(id: Int): ProgrammeOffer? {
        val result = tm.run {
            handle ->  handle.createQuery("SELECT * FROM dbo.programmeOffer WHERE id = :id")
                .bind("id", id)
                .map(offerMapper)
                .one()
        }

        return result
    }
}