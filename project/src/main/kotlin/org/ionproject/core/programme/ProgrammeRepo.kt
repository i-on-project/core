package org.ionproject.core.programme

import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer

interface ProgrammeRepo {
    fun getProgrammes(): List<Programme>
    fun getProgrammeById(id: Int): Programme
    fun getOfferById(id: Int): ProgrammeOffer
}