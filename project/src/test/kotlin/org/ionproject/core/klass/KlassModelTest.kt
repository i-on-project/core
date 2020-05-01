package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class KlassModelTest {
    @Test
    fun createClass_shouldNotThrowExceptionOrChangeValues() {
        val cid = 1
        val acr = "acr"
        val calendarTerm = "1920v"
        val klass = Klass(cid, acr, calendarTerm)
        val klass2 = Klass(cid, null, calendarTerm)

        Assertions.assertEquals(cid, klass.courseId)
        Assertions.assertEquals(acr, klass.courseAcr)
        Assertions.assertEquals(calendarTerm, klass.calendarTerm)
        Assertions.assertNull(klass2.courseAcr)
    }

    @Test
    fun createFullClass_shouldNotThrowExceptionOrChangeValues() {
        val cid = 1
        val acr = "acr"
        val calendarTerm = "1920v"
        val cs1Id = "1d"
        val cs2Id = "2d"
        val cs1 = ClassSection(cid, null, calendarTerm, cs1Id)
        val cs2 = ClassSection(cid, null, calendarTerm, cs2Id)

        val klass = FullKlass(cid, acr, calendarTerm, listOf(cs1, cs2))
        val klass2 = FullKlass(cid, null, calendarTerm, listOf(cs1, cs2))

        Assertions.assertEquals(cid, klass.courseId)
        Assertions.assertEquals(acr, klass.courseAcr)
        Assertions.assertEquals(calendarTerm, klass.calendarTerm)
        Assertions.assertNull(klass2.courseAcr)
        Assertions.assertEquals(cs1, klass.sections[0])
        Assertions.assertEquals(cs2, klass.sections[1])
    }
}