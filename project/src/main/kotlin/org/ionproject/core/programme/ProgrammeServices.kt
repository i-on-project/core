package org.ionproject.core.programme

import org.ionproject.core.common.APP_MODE
import org.ionproject.core.common.MockMode
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.ionproject.core.programme.CoursesDal.IProgrammeRepo
import org.springframework.stereotype.Component

@Component
class ProgrammeServices(@MockMode(APP_MODE) private val repo : IProgrammeRepo) {
    fun readProgrammes() : List<IProgramme> {
        return repo.readProgrammes()
    }

    fun readProgramme(acr : String) : IProgramme {
        return repo.readProgramme(acr)
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

    fun readOffer(id: Int): IProgrammeOffer {
        return repo.readOffer(id)
     }
}