package org.ionproject.core.programme

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.ionproject.core.programme.sql.ProgrammeData
import org.ionproject.core.programme.sql.ProgrammeMapper
import org.ionproject.core.programme.sql.ProgrammeOfferMapper
import org.springframework.stereotype.Component

@Component
class ProgrammeRepoImpl(
  private val tm: TransactionManager,
  private val programmeMapper: ProgrammeMapper,
  private val offerMapper: ProgrammeOfferMapper
) : ProgrammeRepo {

  override fun getProgrammes(): List<Programme> = tm.run { handle ->
    handle.createQuery(ProgrammeData.GET_PROGRAMMES_QUERY)
      .map(programmeMapper)
      .list()
  } as List<Programme>

  override fun getProgrammeById(id: Int): Programme? = tm.run { handle ->
    val res = handle.createQuery(ProgrammeData.GET_PROGRAMME_BY_ID_QUERY)
      .bind(ProgrammeData.ID, id)
      .map(programmeMapper)
      .findOne()

    var programme: Programme? = null

    if (res.isPresent) {
      programme = res.get()
      val offers = handle.createQuery(ProgrammeData.GET_PROGRAMME_OFFERS_QUERY)
        .bind(ProgrammeData.ID, id)
        .map(offerMapper)
        .list()

      programme.offers.addAll(offers)
    }
    programme
  }

  override fun getOfferById(idOffer: Int, idProgramme: Int): ProgrammeOffer? = tm.run { handle ->
    handle.createQuery(ProgrammeData.GET_OFFER_DETAILS_BY_ID)
      .bind(ProgrammeData.ID, idOffer)
      .bind(ProgrammeData.PROGRAMME_ID, idProgramme)
      .map(offerMapper)
      .firstOrNull()
  }
}