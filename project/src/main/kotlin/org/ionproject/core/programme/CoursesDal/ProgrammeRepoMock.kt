package org.ionproject.core.programme.CoursesDal

import org.ionproject.core.common.MockMode
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.stereotype.Component

@Component
@MockMode(true)
class ProgrammeRepoMock : IProgrammeRepo {
    override fun readProgrammes(): List<IProgramme> {
        return listOf(
                Programme(
                        name = "lic eng inf",
                        acronym = "leic",
                        termSize = 6,
                        offers = listOf(
                                ProgrammeOffer(
                                        id = 1,acronym = "POO", termNumber = 2,
                                        credits = 5.5, optional = false,
                                        precedents = listOf(
                                                Course(acronym = "PG", name = "Programação", calendarId = 1)
                                        )
                                ),
                                ProgrammeOffer(
                                        id = 2,acronym = "SI1", termNumber = 2,
                                        credits = 5.5, optional = false
                                )
                        )
                ),
                Programme(
                        name = "mestrado em engenharia informática",
                        acronym = "meic",
                        termSize = 4,
                        offers = listOf(
                                ProgrammeOffer(
                                        id=1,acronym = "SI",termNumber = 1,
                                        credits = 10.0, optional = false
                                )
                        )
                )
        )
    }

    override fun readProgramme(acr: String): IProgramme {
        return Programme(
                name = "lic eng inf",
                acronym = "leic",
                termSize = 6,
                offers = listOf(
                        ProgrammeOffer(
                                id = 1,acronym = "POO", termNumber = 2,
                                credits = 5.5, optional = false,
                                precedents = listOf(
                                        Course(acronym = "PG", name = "Programação", calendarId = 1)
                                )
                        ),
                        ProgrammeOffer(
                                id = 2,acronym = "SI1", termNumber = 2,
                                credits = 5.5, optional = false
                        )
                )
        )
    }

    override fun readOffer(id: Int): IProgrammeOffer {
       return ProgrammeOffer(
               id = 1,acronym = "POO", termNumber = 2,
               credits = 5.5, optional = false,
               precedents = listOf(
                       Course(acronym = "PG", name = "Programação", calendarId = 1)
               )
       )
    }
}