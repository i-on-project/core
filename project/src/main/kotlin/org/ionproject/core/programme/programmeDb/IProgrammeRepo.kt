package org.ionproject.core.programme.programmeDb

import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer


interface IProgrammeRepo {
    fun getProgrammes() : List<IProgramme>
    fun getProgrammeByAcr(acr : String) : IProgramme?
    fun getOfferById(id: Int): IProgrammeOffer?
}