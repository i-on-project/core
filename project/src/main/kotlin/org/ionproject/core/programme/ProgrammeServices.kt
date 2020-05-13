package org.ionproject.core.programme

import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.springframework.stereotype.Component

@Component
class ProgrammeServices(private val repo: ProgrammeRepo) {
  /*
   * Should `ProgrammeSpringController` class hold a reference
   * to repo to avoid making this ""proxy"" useless call's?
   */
  fun getProgrammes(): List<Programme> {
    return repo.getProgrammes()
  }

  fun getProgrammeById(id: Int): Programme? {
    return repo.getProgrammeById(id)
  }

  fun getOfferById(idProgramme: Int, idOffer: Int): ProgrammeOffer? {
    return repo.getOfferById(idOffer, idProgramme)
  }

  fun editProgramme(programme: Programme) {
    TODO("Write API")
  }

  fun addProgramme(programme: Programme) {
    TODO("Write API")
  }

  fun addOffer(acr: String, offer: ProgrammeOffer) {
    TODO("Write API")
  }
}