package org.ionproject.core.readApi.programme

import org.ionproject.core.readApi.programme.model.Programme
import org.ionproject.core.readApi.programme.model.ProgrammeOffer

interface ProgrammeRepo {
    fun getProgrammes(): List<Programme>
    fun getProgrammeById(id: Int): Programme?
    fun getOfferById(idOffer: Int, idProgramme: Int): ProgrammeOffer?
}