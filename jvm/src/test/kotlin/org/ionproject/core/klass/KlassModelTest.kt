package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class KlassModelTest {
    @Test
    fun createClass_shouldMatchEveryValueAppliedToCtor() {
        val cid = 1
        val acr = "acr"
        val calendarTerm = "1920v"
        val klass = Klass(1, cid, acr, calendarTerm)
        val klass2 = Klass(1, cid, null, calendarTerm)

        Assertions.assertEquals(cid, klass.courseId)
        Assertions.assertEquals(acr, klass.courseAcr)
        Assertions.assertEquals(calendarTerm, klass.calendarTerm)
        Assertions.assertNull(klass2.courseAcr)
    }

    @Test
    fun createFullClass_shouldMatchEveryValueAppliedToCtor() {
        val cid = 1
        val acr = "acr"
        val calendarTerm = "1920v"
        val cs1Id = "1d"
        val cs2Id = "2d"
        val cs1 = ClassSection(cid, null, calendarTerm, cs1Id, 1)
        val cs2 = ClassSection(cid, null, calendarTerm, cs2Id, 1)

        val klass = FullKlass(1, cid, acr, calendarTerm, listOf(cs1, cs2))
        val klass2 = FullKlass(1, cid, null, calendarTerm, listOf(cs1, cs2))

        Assertions.assertEquals(cid, klass.courseId)
        Assertions.assertEquals(acr, klass.courseAcr)
        Assertions.assertEquals(calendarTerm, klass.calendarTerm)
        Assertions.assertNull(klass2.courseAcr)
        Assertions.assertEquals(cs1, klass.sections[0])
        Assertions.assertEquals(cs2, klass.sections[1])
    }
}
