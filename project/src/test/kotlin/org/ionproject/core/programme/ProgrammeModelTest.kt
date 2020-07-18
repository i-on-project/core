package org.ionproject.core.programme

import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ProgrammeModelTest {
    @Test
    fun createProgramme_shouldMatchEveryValueAppliedToCtor() {
        val id = 1
        val name = "licenciatura eng. inf."
        val acr = "LEIC"
        val size = 6
        val p = Programme(id, name, acr, 6, mutableListOf())

        Assertions.assertEquals(id, p.id)
        Assertions.assertEquals(acr, p.acronym)
        Assertions.assertEquals(name, p.name)
        Assertions.assertEquals(size, p.termSize)
    }

    @Test
    fun createProgrammeOffer_shouldMatchEveryValueAppliedToCtor() {
        val id = 1
        val acr = "WAD"
        val pid = 1
        val cid = 2
        val termNumber = listOf(3)
        val optional = false
        val p = ProgrammeOffer(id, acr, pid, cid, termNumber, optional)

        Assertions.assertEquals(id, p.id)
        Assertions.assertEquals(acr, p.courseAcr)
        Assertions.assertEquals(cid, p.courseId)
        Assertions.assertEquals(pid, p.programmeId)
        Assertions.assertEquals(termNumber, p.termNumber)
        Assertions.assertEquals(optional, p.optional)
    }
}

