package org.ionproject.core.calendar

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test

internal class CalendarErrorTest : ControllerTester() {

    companion object {
        val cid = 2
        val calterm = "1718v"
        val sid = "1D"
        val calendarByCalTerm =
            Uri.forCalendarByClass(cid, calterm)
        val calendarByClassSection =
            Uri.forCalendarByClassSection(cid, calterm, sid)
        val calendarComponentByCalTerm =
            Uri.forCalendarComponentByClass(cid, calterm, "1")
        val calendarComponentByClassSection =
            Uri.forCalendarComponentByClassSection(cid, calterm, sid, "1")

        val validLinks = listOf(
            calendarByCalTerm,
            calendarByClassSection,
            calendarComponentByCalTerm
        )

        val notFoundCalendarByCalTerm =
            Uri.forCalendarByClass(cid, "5050505050")
        val notFoundCalendarByClassSection =
            Uri.forCalendarByClassSection(-1, calterm, sid)
        val notFoundCalendarComponentByCalTerm =
            Uri.forCalendarComponentByClass(cid, calterm, "-1")
        val notFoundCalendarComponentByClassSection =
            Uri.forCalendarComponentByClassSection(cid, calterm, "impossibleClassName", "1")

        val invalidLinks = listOf(
            notFoundCalendarByCalTerm,
            notFoundCalendarByClassSection,
            notFoundCalendarComponentByCalTerm,
            notFoundCalendarComponentByClassSection
        )
    }

    /**
     * Not found calendar term with
     * calterm = 1921231230i
     */
    @Test
    fun getInvalidCalendar() {
        for (link in invalidLinks) {
            doGet(link) {
                header("Authorization", readTokenTest)
            }.andDo { println() }
                .andExpect {
                    status { isNotFound() }
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
            doGet(link) {
                accept = Media.MEDIA_HOME
                header("Authorization", readTokenTest)
            }.andDo { println() }
                .andExpect { status { isNotAcceptable() } }
                .andReturn()
        }
    }
}
