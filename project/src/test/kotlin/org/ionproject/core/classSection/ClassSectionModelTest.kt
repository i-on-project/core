package org.ionproject.core.classSection

import org.ionproject.core.common.model.ClassSection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ClassSectionModelTest {
    @Test
    fun createClassSection_shouldNotThrowExceptionOrChangeValues() {
        val cid = 1
        val acr = "acr"
        val calendarTerm = "1920v"
        val classSectionId = "1d"
        val cs = ClassSection(cid, acr, calendarTerm, classSectionId,0)
        val cs2 = ClassSection(cid, null, calendarTerm, classSectionId,0)

        Assertions.assertEquals(cid, cs.courseId)
        Assertions.assertEquals(acr, cs.courseAcr)
        Assertions.assertEquals(calendarTerm, cs.calendarTerm)
        Assertions.assertEquals(classSectionId, cs.id)
        Assertions.assertNull(cs2.courseAcr)
    }
}