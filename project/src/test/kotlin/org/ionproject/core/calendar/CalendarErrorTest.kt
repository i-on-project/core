package org.ionproject.core.calendar


import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Test

internal class CalendarErrorTest : ControllerTester() {

    companion object {
        val calendarByCalTerm = Uri.forCalendarByClass(1, "1718v")
        val calendarByClassSection = Uri.forCalendarByClassSection(1, "1718v", "1D")
        val calendarComponentByCalTerm = Uri.forCalendarComponentByClass(1, "1718v", "1")
        val calendarComponentByClassSection = Uri.forCalendarComponentByClassSection(1, "1718v", "1D", "1")
        val validLinks = listOf(calendarByCalTerm, calendarByClassSection, calendarComponentByCalTerm)


        val notFoundCalendarByCalTerm = Uri.forCalendarByClass(1, "5050505050")
        val notFoundCalendarByClassSection = Uri.forCalendarByClassSection(-1, "1718v", "1D")
        val notFoundCalendarComponentByCalTerm = Uri.forCalendarComponentByClass(1, "1718v", "-1")
        val notFoundCalendarComponentByClassSection = Uri.forCalendarComponentByClassSection(1, "1718v", "impossibleClassName", "1")
        val invalidLinks = listOf(notFoundCalendarByCalTerm, notFoundCalendarByClassSection, notFoundCalendarComponentByCalTerm, notFoundCalendarComponentByClassSection)
    }

    /**
     * Not found calendar term with
     * calterm = 1921231230i
     */
    @Test
    fun getInvalidCalendar() {
        for (link in invalidLinks) {
            doGet(link)
                    .andDo { print() }
                    .andExpect {
                        status { isNotFound }
                    }.andReturn()
        }
    }


    /**
     * Requesting course with a content-type "JSON-HOME"
     * that is not supported.
     */
    @Test
    fun getCalendarWithInvalidContentType() {
        for (link in validLinks) {
            doGet(link) { accept = Media.MEDIA_HOME }
                    .andDo { print() }
                    .andExpect { status { isNotAcceptable } }
                    .andReturn()
        }
    }
}
