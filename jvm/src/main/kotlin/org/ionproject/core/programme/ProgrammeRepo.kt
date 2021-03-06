package org.ionproject.core.programme

import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer

interface ProgrammeRepo {
    fun getProgrammes(page: Int, limit: Int): List<Programme>
    fun getProgrammeById(id: Int): Programme
    fun getProgrammeOffers(id: Int, page: Int, limit: Int): List<ProgrammeOffer>
    fun getOfferById(idProgramme: Int, idOffer: Int): ProgrammeOffer
}
