package org.ionproject.core.calendar

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Parameter
import net.fortuna.ical4j.model.ParameterList
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.PropertyList
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VToDo
import net.fortuna.ical4j.model.parameter.Language
import net.fortuna.ical4j.model.property.Attach
import net.fortuna.ical4j.model.property.Categories
import net.fortuna.ical4j.model.property.Created
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.DtEnd
import net.fortuna.ical4j.model.property.DtStamp
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.Due
import net.fortuna.ical4j.model.property.Location
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.RRule
import net.fortuna.ical4j.model.property.Summary
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Version
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.fluentAdd
import org.ionproject.core.removeWhitespace
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import java.net.URI
import java.nio.charset.StandardCharsets

internal class CalendarControllerTest : ControllerTester() {
    companion object {
        val courseID = 2
        val calTerm = "1920v"
        val classSection = "LI61D"
        val componentIDClassSection = "15"
        val componentIdClass = "1f"

        val calendarByClass =
            """
                BEGIN:VCALENDAR
                PRODID:/v0/courses/2/classes/1920v
                VERSION:2.0
                BEGIN:VTODO
                UID:1f
                DTSTAMP:20200101T163530Z
                SUMMARY;LANGUAGE=en-US:[WAD]:Assignment#1
                DESCRIPTION;LANGUAGE=en-US:Thefirstassignment.ThegoalistoimplementanHTTPAPI...ATTACH:https://tools.ietf.org/html/rfc7231
                CREATED:20200101T163530Z
                DUE:20200419T235900Z
                CATEGORIES;LANGUAGE=pt-PT:Laboratório
                CATEGORIES;LANGUAGE=en-US:Laboratory
                CATEGORIES;LANGUAGE=en-GB:Laboratory
                END:VTODOBEGIN:VTODOUID:20
                DTSTAMP:20200101T163530Z
                SUMMARY;
                LANGUAGE=en-US:[WAD]:Assignment#2
                DESCRIPTION;LANGUAGE=en-US:Thesecondassignment.ThegoalistoimplementaWebClientfortheAPI...
                CREATED:20200101T163530Z
                DUE:20200525T235900Z
                CATEGORIES;LANGUAGE=pt-PT:Laboratório
                CATEGORIES;LANGUAGE=en-US:Laboratory
                CATEGORIES;LANGUAGE=en-GB:Laboratory
                END:VTODOBEGIN:VTODOUID:21
                DTSTAMP:20200101T163530Z
                SUMMARY;LANGUAGE=en-US:[WAD]:Assignment#3DESCRIPTION;LANGUAGE=en-US:Thethirdandfinalassignment.Wrappingitup...
                CREATED:20200101T163530Z
                DUE:20200705T235900Z
                CATEGORIES;LANGUAGE=pt-PT:Laboratório
                CATEGORIES;LANGUAGE=en-US:Laboratory
                CATEGORIES;LANGUAGE=en-GB:Laboratory
                END:VTODOBEGIN:VEVENTUID:2c
                DTSTAMP:20200101T163530Z
                SUMMARY;LANGUAGE=en-US:1stExamWADDESCRIPTION;LANGUAGE=en-US:NormalseasonexamforWAD-1920v
                CREATED:20200101T163530Z
                CATEGORIES;LANGUAGE=pt-PT:Exame
                CATEGORIES;LANGUAGE=en-US:Exam
                CATEGORIES;LANGUAGE=en-GB:Exam
                DTSTART:20200619T180000Z
                DTEND:20200619T193000Z
                LOCATION:RoomA.2.10
                END:VEVENTBEGIN:VEVENTUID:2d
                DTSTAMP:20200101T163530Z
                SUMMARY;LANGUAGE=en-US:2ndExamWADDESCRIPTION;LANGUAGE=en-US:SecondseasonexamforWAD-1920v
                CREATED:20200101T163530Z
                CATEGORIES;LANGUAGE=pt-PT:Exame
                CATEGORIES;LANGUAGE=en-US:Exam
                CATEGORIES;LANGUAGE=en-GB:Exam
                DTSTART:20200701T100000Z
                DTEND:20200701T123000Z
                LOCATION:RoomA.2.10
                END:VEVENTEND:VCALENDAR
            """

        val calendarByClassSection =
            """
                BEGIN:VCALENDAR
                PRODID:/v0/courses/2/classes/1920v/LI61D
                VERSION:2.0
                BEGIN:VEVENT
                UID:15
                DTSTAMP:20200101T163530Z
                SUMMARY;LANGUAGE=en-US:WAD Lecture
                DESCRIPTION;LANGUAGE=en-US:Lectures of the WAD curricular unit for the 19
                 20v-LI61D Class section
                CREATED:20200101T163530Z
                CATEGORIES;LANGUAGE=pt-PT:Aula
                CATEGORIES;LANGUAGE=en-US:Lecture
                CATEGORIES;LANGUAGE=en-GB:Lecture
                DTSTART:20190201T110000Z
                DTEND:20190201T123000Z
                LOCATION:Room G.0.1
                RRULE:FREQ=WEEKLY;UNTIL=20190610T000000Z;BYDAY=MO
                END:VEVENT
                BEGIN:VEVENT
                UID:16
                DTSTAMP:20200101T163530Z
                SUMMARY;LANGUAGE=en-US:WAD Lecture
                DESCRIPTION;LANGUAGE=en-US:Lectures of the WAD curricular unit for the 19
                 20v-LI61D Class section
                CREATED:20200101T163530Z
                CATEGORIES;LANGUAGE=pt-PT:Aula
                CATEGORIES;LANGUAGE=en-US:Lecture
                CATEGORIES;LANGUAGE=en-GB:Lecture
                DTSTART:20190201T110000Z
                DTEND:20190201T140000Z
                LOCATION:Room G.0.11
                RRULE:FREQ=WEEKLY;UNTIL=20190610T000000Z;BYDAY=TH
                END:VEVENT
                END:VCALENDAR
            """

        val classCalendarCal4j = buildCalendarForClass()
        val classSectionCalendarCal4j = buildCalendarForClassSection()

        /**
         * Builds a cal4j calendar with the data defined by the Read API
         * Cal Mock Data for comparison with the one generated
         * by the core calendar.
         */
        private fun buildCalendarForClass(): Calendar {
            val calendar = Calendar() // ical4j calendar (not the one implemented by the group)
            calendar.properties.add(ProdId("/v0/courses/$courseID/classes/$calTerm"))
            calendar.properties.add(Version.VERSION_2_0)

            val enLang = buildParameterList(Language("en-US"))
            val gbLang = buildParameterList(Language("en-GB"))
            val ptLang = buildParameterList(Language("pt-PT"))
            val rfcAttachment = URI.create("https://tools.ietf.org/html/rfc7231")

            val todoWad1Assignment = VToDo(
                PropertyList<Property>().fluentAdd(
                    Uid("1f"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "[WAD]: Assignment #1"),
                    Description(enLang, "The first assignment. The goal is to implement an HTTP API..."),
                    Attach(rfcAttachment),
                    Created("20200101T163530Z"),
                    Due("20200419T235900Z"),
                    Categories(ptLang, "Laboratório"),
                    Categories(enLang, "Laboratory"),
                    Categories(gbLang, "Laboratory")
                ) as PropertyList<Property>
            )

            val todoWad2Assignment = VToDo(
                PropertyList<Property>().fluentAdd(
                    Uid("20"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "[WAD]: Assignment #2"),
                    Description(enLang, "The second assignment. The goal is to implement a Web Client for the API..."),
                    Created("20200101T163530Z"),
                    Due("20200525T235900Z"),
                    Categories(ptLang, "Laboratório"),
                    Categories(enLang, "Laboratory"),
                    Categories(gbLang, "Laboratory")
                ) as PropertyList<Property>
            )

            val todoWad3Assignment = VToDo(
                PropertyList<Property>().fluentAdd(
                    Uid("21"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "[WAD]: Assignment #3"),
                    Description(enLang, "The third and final assignment. Wrapping it up..."),
                    Created("20200101T163530Z"),
                    Due("20200705T235900Z"),
                    Categories(ptLang, "Laboratório"),
                    Categories(enLang, "Laboratory"),
                    Categories(gbLang, "Laboratory")
                ) as PropertyList<Property>
            )

            val eventWad1Exam = VEvent(
                PropertyList<Property>().fluentAdd(
                    Uid("2c"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "1st Exam WAD"),
                    Description(enLang, "Normal season exam for WAD-1920v"),
                    Created("20200101T163530Z"),
                    Categories(ptLang, "Exame"),
                    Categories(enLang, "Exam"),
                    Categories(gbLang, "Exam"),
                    DtStart("20200619T180000Z"),
                    DtEnd("20200619T193000Z"),
                    Location("Room A.2.10")
                ) as PropertyList<Property>
            )

            val eventWad2Exam = VEvent(
                PropertyList<Property>().fluentAdd(
                    Uid("2d"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "2nd Exam WAD"),
                    Description(enLang, "Second season exam for WAD-1920v"),
                    Created("20200101T163530Z"),
                    Categories(ptLang, "Exame"),
                    Categories(enLang, "Exam"),
                    Categories(gbLang, "Exam"),
                    DtStart("20200701T100000Z"),
                    DtEnd("20200701T123000Z"),
                    Location("Room A.2.10")
                ) as PropertyList<Property>
            )

            calendar.components.fluentAdd(
                todoWad1Assignment,
                todoWad2Assignment,
                todoWad3Assignment,
                eventWad1Exam,
                eventWad2Exam
            )

            return calendar
        }

        fun buildCalendarForClassSection(): Calendar {
            val calendar = Calendar()
            calendar.properties.add(ProdId("/v0/courses/$courseID/classes/$calTerm/$classSection"))
            calendar.properties.add(Version.VERSION_2_0)

            val enLang = buildParameterList(Language("en-US"))
            val gbLang = buildParameterList(Language("en-GB"))
            val ptLang = buildParameterList(Language("pt-PT"))

            val eventWadLectureMonday = VEvent(
                PropertyList<Property>().fluentAdd(
                    Uid("15"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "WAD Lecture"),
                    Description(enLang, "Lectures of the WAD curricular unit for the 1920v-LI61D Class section"),
                    Created("20200101T163530Z"),
                    Categories(ptLang, "Aula"),
                    Categories(enLang, "Lecture"),
                    Categories(gbLang, "Lecture"),
                    DtStart("20190201T110000Z"),
                    DtEnd("20190201T123000Z"),
                    Location("Room G.0.1"),
                    RRule("FREQ=WEEKLY;UNTIL=20190610T000000Z;BYDAY=MO")
                ) as PropertyList<Property>
            )

            val eventWadLectureWednesday = VEvent(
                PropertyList<Property>().fluentAdd(
                    Uid("16"),
                    DtStamp("20200101T163530Z"),
                    Summary(enLang, "WAD Lecture"),
                    Description(enLang, "Lectures of the WAD curricular unit for the 1920v-LI61D Class section"),
                    Created("20200101T163530Z"),
                    Categories(ptLang, "Aula"),
                    Categories(enLang, "Lecture"),
                    Categories(gbLang, "Lecture"),
                    DtStart("20190201T110000Z"),
                    DtEnd("20190201T140000Z"),
                    Location("Room G.0.11"),
                    RRule("FREQ=WEEKLY;UNTIL=20190610T000000Z;BYDAY=TH")
                ) as PropertyList<Property>
            )

            calendar.components.fluentAdd(
                eventWadLectureMonday,
                eventWadLectureWednesday
            )

            return calendar
        }

        // Adds parameters to a component property
        fun buildParameterList(param: Parameter): ParameterList {
            val paramList = ParameterList()
            paramList.add(param)
            return paramList
        }
    }

    /**
     * Checking the endpoints for valid siren composition
     */
    @Test
    fun getCalendarByClass() {
        isValidSiren(Uri.forCalendarByClass(courseID, calTerm)).andReturn()
    }

    @Test
    fun getCalendarByClassSection() {
        isValidSiren(Uri.forCalendarByClassSection(courseID, calTerm, classSection)).andReturn()
    }

    @Test
    fun getCalendarComponentByClass() {
        mocker.get(Uri.forCalendarComponentByClass(courseID, calTerm, componentIdClass)) {
            accept = Media.MEDIA_SIREN
            header("Authorization", readTokenTest)
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
        }
    }

    @Test
    fun getCalendarComponentByClassSection() {
        mocker.get(Uri.forCalendarComponentByClassSection(courseID, calTerm, classSection, componentIDClassSection)) {
            accept = Media.MEDIA_SIREN
            header("Authorization", readTokenTest)
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
        }
    }

    /**
     * text/calendar content-type is generated from the
     * JSON object with the use of the calendar message converter
     */
    @Test
    fun getCalendarByClass_And_Compare() {
        val result = doGet(Uri.forCalendarByClass(courseID, calTerm)) {
            accept = Media.MEDIA_TEXT_CALENDAR
            header("Authorization", readTokenTest)
        }.andReturn()
            .response
            .getContentAsString(StandardCharsets.UTF_8)

        /**
         * Had a small problem with the way the messageConverter
         * added newlines, the way obtained by the browser depending on the
         * oparating system and the
         * way multiline strings added new lines, for compatibility
         * in all situation the following regex removes all lines and spaces
         * from the strings.
         */
        val expected: String = calendarByClass.removeWhitespace()
        val obtained: String = result.removeWhitespace()
        Assertions.assertEquals(expected, obtained)
    }

    @Test
    fun getCalendarByClassSection_And_Compare() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection)) {
            accept = Media.MEDIA_TEXT_CALENDAR
            header("Authorization", readTokenTest)
        }.andReturn()
            .response
            .getContentAsString(StandardCharsets.UTF_8)

        val expected: String = calendarByClassSection.removeWhitespace()
        val obtained: String = result.removeWhitespace()
        Assertions.assertEquals(expected, obtained)
    }

    /**
     * Builds ical4j calendar and compares with the core calendar
     */
    @Test
    fun checkIfValidCalClass() {
        // Get the core calendar representation
        val result = doGet(Uri.forCalendarByClass(courseID, calTerm)) {
            accept = Media.MEDIA_TEXT_CALENDAR
            header("Authorization", readTokenTest)
        }.andReturn()
            .response
            .getContentAsString(StandardCharsets.UTF_8)

        Assertions.assertEquals(
            classCalendarCal4j.toString().removeWhitespace(),
            result.removeWhitespace()
        )
    }

    @Test
    fun checkIfValidCalClassSection() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection)) {
            accept = Media.MEDIA_TEXT_CALENDAR
            header("Authorization", readTokenTest)
        }.andReturn()
            .response
            .getContentAsString(StandardCharsets.UTF_8)

        Assertions.assertEquals(
            classSectionCalendarCal4j.toString().removeWhitespace(),
            result.removeWhitespace()
        )
    }
}
