package org.ionproject.core.programme.CoursesDal

import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer


interface IProgrammeRepo {
    fun readProgrammes() : List<IProgramme>
    fun readProgramme(acr : String) : IProgramme
    fun readOffer(id: Int): IProgrammeOffer
}