package org.ionproject.core.programme.CoursesDal

import org.ionproject.core.common.MockMode
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.stereotype.Component

@Component
@MockMode(false)
class ProgrammeRepo : IProgrammeRepo {
    override fun readProgrammes(): List<IProgramme> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readProgramme(acr: String): IProgramme {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readOffer(id: Int): IProgrammeOffer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}