package org.ionproject.core.programme

import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.springframework.stereotype.Component

@Component
class ProgrammeServices(private val repo : ProgrammeRepoImpl) {
    /*
     * Should `ProgrammeSpringController` class hold a reference
     * to repo to avoid making this ""proxy"" useless call's?
     */
    fun getProgrammes() : List<Programme> {
        return repo.getProgrammes()
    }

    fun getProgrammeById(id : Int) : Programme {
        return repo.getProgrammeById(id)
    }

    fun getOfferById(id: Int): ProgrammeOffer {
        return repo.getOfferById(id)
    }

    fun editProgramme(programme : Programme) {
        TODO("Write API")
    }

    fun addProgramme(programme : Programme) {
        TODO("Write API")
    }

    fun addOffer(acr : String, offer : ProgrammeOffer) {
        TODO("Write API")
    }
}