package org.ionproject.core.programme

import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.ionproject.core.programme.programmeDb.IProgrammeRepo
import org.springframework.stereotype.Component

@Component
class ProgrammeServices(private val repo : IProgrammeRepo) {
    fun getProgrammes() : List<IProgramme> {
        return repo.getProgrammes()
    }

    fun getProgrammeByAcr(acr : String) : IProgramme? {
        return repo.getProgrammeByAcr(acr)
    }

    fun getOfferById(id: Int): IProgrammeOffer? {
        return repo.getOfferById(id)
    }

    fun editProgramme(programme : IProgramme) {
        TODO()
    }

    fun addProgramme(programme : IProgramme) {
        TODO()
    }

    fun addOffer(acr : String, offer : IProgrammeOffer) {
        TODO()
    }
}