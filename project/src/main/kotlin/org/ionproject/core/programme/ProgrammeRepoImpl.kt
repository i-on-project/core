package org.ionproject.core.programme

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.programme.mappers.ProgrammeMapper
import org.ionproject.core.programme.mappers.ProgrammeOfferMapper
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.springframework.stereotype.Component

@Component
class ProgrammeRepoImpl(private val tm: TransactionManager,
                        private val programmeMapper: ProgrammeMapper,
                        private val offerMapper: ProgrammeOfferMapper) : ProgrammeRepo {

    override fun getProgrammes(): List<Programme> = tm.run { handle ->
        handle.createQuery("SELECT * FROM dbo.programme")
            .map(programmeMapper)
            .list()
    } as List<Programme>

    override fun getProgrammeById(id: Int): Programme? = tm.run { handle ->
        val res = handle.createQuery("SELECT id, acronym, name, termsize FROM dbo.programme WHERE id= :id")
            .bind("id", id)
            .map(programmeMapper)
            .findOne()

        var programme: Programme? = null

        if (res.isPresent) {
            programme = res.get()
            val offers = handle.createQuery(
                """ SELECT po.*,co.acronym AS courseAcr FROM dbo.programmeOffer AS po INNER JOIN dbo.course AS co
                        ON po.courseId=co.id
                        WHERE programmeId = :id 
                        ORDER BY po.id
                        """.trimIndent())
                .bind("id", id)
                .map(offerMapper)
                .list()

            programme.offers.addAll(offers)
        }
        programme
    }

    override fun getOfferById(idOffer: Int, idProgramme: Int): ProgrammeOffer? = tm.run { handle ->
        handle.createQuery("select po.id as id, acronym as courseAcr, programmeid, courseid, termnumber, optional from dbo.programmeoffer po join dbo.course c on po.courseid=c.id where po.id=:id and programmeid=:programmeid;")
            .bind("id", idOffer)
            .bind("programmeid", idProgramme)
            .map(offerMapper)
            .firstOrNull()
    }
}