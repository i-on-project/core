package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.klass.model.Klass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime.now

internal class CalendarTermModelTest {
    @Test
    fun createCalendarTerm() {
        val calTermId = "1920i"
        val time = now()
        val calObject = CalendarTerm("1920i", time, time)
        val classList = mutableListOf<Klass>()

        Assertions.assertEquals(calTermId, calObject.calTermId)
        Assertions.assertEquals(time, calObject.startDate)
        Assertions.assertEquals(time, calObject.endDate)
        Assertions.assertEquals(classList, calObject.classes)
    }
}
