package org.ionproject.core.programme

import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer

interface ProgrammeRepo {
    fun getProgrammes(): List<Programme>
    fun getProgrammeById(id: Int): Programme?
    fun getOfferById(idProgramme: Int, idOffer: Int): ProgrammeOffer?
}