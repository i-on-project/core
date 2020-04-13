package org.ionproject.core

import org.ionproject.core.common.model.Course
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.ionproject.core.programme.programmeDb.IProgrammeRepo
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
class ProgrammeRepoMock : IProgrammeRepo {
    override fun getProgrammes(): List<IProgramme> {
        return listOf(
                Programme(
                        id = 1,
                        name = "lic eng inf",
                        acronym = "leic",
                        termSize = 6,
                        offers = listOf(
                                ProgrammeOffer(
                                        id = 1,programmeId = 1,courseId = 1,courseAcr = "POO",
                                        termNumber = 2, optional = false
                                ),
                                ProgrammeOffer(
                                        id = 2,programmeId = 1,courseId = 2,courseAcr = "SI1", termNumber = 2, optional = false
                                )
                        )
                ),
                Programme(
                        id = 2,
                        name = "mestrado em engenharia informática",
                        acronym = "meic",
                        termSize = 4,
                        offers = listOf()
                )
        )
    }

    override fun getProgrammeByAcr(acr: String): IProgramme {
        return Programme(
                        id = 1,
                        name = "lic eng inf",
                        acronym = "leic",
                        termSize = 6,
                        offers = listOf(
                                ProgrammeOffer(
                                        id = 1,programmeId = 1,courseId = 1,courseAcr = "POO",
                                        termNumber = 2, optional = false
                                ),
                                ProgrammeOffer(
                                        id = 2,programmeId = 1,courseId = 2,courseAcr = "SI1", termNumber = 2, optional = false
                                )
                        )
                )

    }

    override fun getOfferById(id: Int): IProgrammeOffer {
       return ProgrammeOffer(
               id = 1,programmeId = 1,courseId = 1,courseAcr = "POO",
               termNumber = 2, optional = false
       )
    }
}