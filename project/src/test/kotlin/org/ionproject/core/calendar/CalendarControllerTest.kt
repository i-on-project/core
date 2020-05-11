package org.ionproject.core.calendar


import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class CalendarControllerTest : ControllerTester() {
    companion object {
        val courseID = 1
        val calTerm = "1718v"
        val classSection = "1D"

        val calendarByClass = """
                BEGIN:VCALENDAR
                PRODID:/v0/courses/1/classes/1718v
                VERSION:2.0
                BEGIN:VJOURNAL
                UID:1
                DTSTAMP:20200511T162630Z
                SUMMARY;LANGUAGE=pt-PT:uma sinopse
                SUMMARY;LANGUAGE=en-US:some summary
                DESCRIPTION;LANGUAGE=en-US:this is a description
                ATTACH:https://www.google.com
                DTSTART:20200410T140000Z
                CREATED:20200511T162630Z
                CATEGORIES;LANGUAGE=pt-PT:EXAME
                CATEGORIES;LANGUAGE=en-US:EXAM
                CATEGORIES;LANGUAGE=en-GB:EXAM
                END:VJOURNAL
                BEGIN:VTODO
                UID:2
                DTSTAMP:20200511T162630Z
                SUMMARY;LANGUAGE=en-US:do the thing
                DESCRIPTION;LANGUAGE=en-US:I have to do the thing soon enough
                ATTACH:https://www.google.com
                CREATED:20200511T162630Z
                DUE:20210619T140000Z
                CATEGORIES;LANGUAGE=pt-PT:EXAME
                CATEGORIES;LANGUAGE=en-US:EXAM
                END:VTODO
                BEGIN:VTODO
                UID:3
                DTSTAMP:20200511T162630Z
                SUMMARY;LANGUAGE=en-US:don't do the thing
                DESCRIPTION;LANGUAGE=en-US:I have to do the thing soon enough
                CREATED:20200511T162630Z
                DUE:20210619T140000Z
                CATEGORIES;LANGUAGE=pt-PT:EXAME
                CATEGORIES;LANGUAGE=en-US:EXAM
                END:VTODO
                BEGIN:VEVENT
                UID:4
                DTSTAMP:20200511T162630Z
                SUMMARY;LANGUAGE=en-US:1st exam WAD
                DESCRIPTION;LANGUAGE=en-US:Normal season exam for WAD-1920v
                CREATED:20200511T162630Z
                CATEGORIES;LANGUAGE=en-GB:EXAM
                DTSTART:20200619T140000Z
                DTEND:20200619T150000Z
                END:VEVENT
                END:VCALENDAR
                """.replace("\\s".toRegex(), "")

        val calendarByClassSection =
                """BEGIN:VCALENDAR
                PRODID:/v0/courses/1/classes/1718v/1D
                VERSION:2.0
                END:VCALENDAR""".replace("\\s".toRegex(), "")
    }

    @Test
    fun getCalendarByClass() {
        isValidSiren(Uri.forCalendarByClass(courseID, calTerm)).andReturn()
    }

    @Test
    fun getCalendarByClassSection() {
        isValidSiren(Uri.forCalendarByClassSection(courseID, calTerm, classSection)).andReturn()
    }

    /**
     * text/calendar content-type is generated from the
     * JSON object with the use of the calendar message converter
     */
    @Test
    fun getCalendarByClass_And_Compare() {
        val result = doGet(Uri.forCalendarByClass(courseID, calTerm))
            {accept = Media.MEDIA_TEXT_CALENDAR}.
                andReturn()
                .response.contentAsString
                .replace("\\s".toRegex(), "")

        Assertions.assertEquals(calendarByClass, result)
    }

    @Test
    fun getCalendarByClassSection_And_Compare() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
            {accept = Media.MEDIA_TEXT_CALENDAR}
                .andReturn()
                .response
                .contentAsString
                .replace("\\s".toRegex(), "")

        Assertions.assertEquals(calendarByClassSection, result)
    }

}