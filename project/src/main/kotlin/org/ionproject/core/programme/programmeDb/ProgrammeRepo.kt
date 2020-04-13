package org.ionproject.core.programme.programmeDb

import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class ProgrammeRepo : IProgrammeRepo {
    override fun getProgrammes(): List<IProgramme> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProgrammeByAcr(acr: String): IProgramme? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOfferById(id: Int): IProgrammeOffer? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}