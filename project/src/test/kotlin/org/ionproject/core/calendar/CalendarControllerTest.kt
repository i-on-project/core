package org.ionproject.core.calendar


import net.fortuna.ical4j.model.*
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VToDo
import net.fortuna.ical4j.model.parameter.Language
import net.fortuna.ical4j.model.property.*
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ComponentList
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.ParameterList
import org.ionproject.core.utils.PropertyList
import org.ionproject.core.utils.output
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URI

internal class CalendarControllerTest : ControllerTester() {
    companion object {
        val courseID = 2
        val calTerm = "1718v"
        val classSection = "1D"
        val classComponentID = "2"
        val classSectionComponentID = "6"

        val stampDate = "20200101T163530Z"

        val calendarByClass =
            """BEGIN:VCALENDAR
PRODID:/v0/courses/2/classes/1718v
VERSION:2.0
BEGIN:VTODO
UID:2
DTSTAMP:$stampDate
SUMMARY;LANGUAGE=sq:[WAD]: Assignment #1
DESCRIPTION;LANGUAGE=sq:The first assignment. The goal is to implement an H
 TTP API...
ATTACH:https://tools.ietf.org/html/rfc7231
CREATED:$stampDate
DUE:20210419T235900Z
CATEGORIES;LANGUAGE=pt-PT:Exame,Aula
END:VTODO
BEGIN:VTODO
UID:3
DTSTAMP:$stampDate
SUMMARY;LANGUAGE=sq:[WAD]: Assignment #2
DESCRIPTION;LANGUAGE=sq:The second assignment. The goal is to implement a W
 eb Client for the API...
CREATED:$stampDate
DUE:20210612T235900Z
CATEGORIES;LANGUAGE=pt-PT:Exame,Aula
END:VTODO
BEGIN:VEVENT
UID:4
DTSTAMP:$stampDate
SUMMARY;LANGUAGE=sq:1st Exam WAD
DESCRIPTION;LANGUAGE=sq:Normal season exam for WAD-1920v
CREATED:$stampDate
CATEGORIES;LANGUAGE=en-GB:Exam
DTSTART:20200619T180000Z
DTEND:20200619T193000Z
RRULE:FREQ=WEEKLY;BYDAY=FR
END:VEVENT
BEGIN:VEVENT
UID:5
DTSTAMP:$stampDate
SUMMARY;LANGUAGE=sq:2st Exam WAD
DESCRIPTION;LANGUAGE=sq:Second season exam for WAD-1920v
CREATED:$stampDate
CATEGORIES;LANGUAGE=en-GB:Exam
DTSTART:20200701T100000Z
DTEND:20200701T123000Z
RRULE:FREQ=WEEKLY;BYDAY=WE
END:VEVENT
END:VCALENDAR
"""

        val calendarByClassSection =
            """BEGIN:VCALENDAR
PRODID:/v0/courses/2/classes/1718v/1D
VERSION:2.0
BEGIN:VEVENT
UID:6
DTSTAMP:$stampDate
SUMMARY;LANGUAGE=sq:WAD Monday Lecture
DESCRIPTION;LANGUAGE=sq:Lectures of the WAD curricular unit for the 1718v-1D 
 Class section
CREATED:$stampDate
CATEGORIES;LANGUAGE=en-GB:Exam
DTSTART:20200210T100000Z
DTEND:20200210T123000Z
RRULE:FREQ=WEEKLY;BYDAY=MO
END:VEVENT
BEGIN:VEVENT
UID:7
DTSTAMP:$stampDate
SUMMARY;LANGUAGE=sq:WAD Wednesday Lecture
DESCRIPTION;LANGUAGE=sq:Lectures of the WAD curricular unit for the 1718v-1
 D Class section
CREATED:$stampDate
CATEGORIES;LANGUAGE=en-GB:Exam
DTSTART:20200212T100000Z
DTEND:20200212T123000Z
RRULE:FREQ=WEEKLY;BYDAY=WE
END:VEVENT
END:VCALENDAR
"""

        val classCalendarCal4j = buildCalendarForClass()
        val classSectionCalendarCal4j = buildCalendarForClassSection()
        val classComponentCal4j = buildComponentForClass()
        val classSectionComponentCal4j = buildComponentForClassSection()

        /**
         * Builds a cal4j calendar with the data defined by the Read API
         * Cal Mock Data for comparison with the one generated
         * by the core calendar.
         *
         * https://github.com/ical4j/ical4j
         */
        fun buildCalendarForClass(): Calendar =
            Calendar( //ical4j calendar (not the one implemented by the group)
                PropertyList(
                    ProdId("/v0/courses/$courseID/classes/$calTerm"),
                    Version.VERSION_2_0
                ),
                ComponentList(
                    VToDo(
                        PropertyList(
                            Uid("2"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "[WAD]: Assignment #1"),
                            Description(
                                ParameterList(Language("sq")),
                                "The first assignment. The goal is to implement an HTTP API..."
                            ),
                            Attach(URI("https://tools.ietf.org/html/rfc7231")),
                            Created(stampDate),
                            Due("20210419T235900Z"),
                            Categories(ParameterList(Language("pt-PT")), TextList(arrayOf("Exame", "Aula")))
                        )
                    ),
                    VToDo(
                        PropertyList(
                            Uid("3"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "[WAD]: Assignment #2"),
                            Description(
                                ParameterList(Language("sq")),
                                "The second assignment. The goal is to implement a Web Client for the API..."
                            ),
                            Created(stampDate),
                            Due("20210612T235900Z"),
                            Categories(ParameterList(Language("pt-PT")), TextList(arrayOf("Exame", "Aula")))
                        )
                    ),
                    VEvent(
                        PropertyList(
                            Uid("4"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "1st Exam WAD"),
                            Description(ParameterList(Language("sq")), "Normal season exam for WAD-1920v"),
                            Created(stampDate),
                            Categories(ParameterList(Language("en-GB")), "Exam"),
                            DtStart("20200619T180000Z"),
                            DtEnd("20200619T193000Z"),
                            RRule("FREQ=WEEKLY;BYDAY=FR")
                        )
                    ),
                    VEvent(
                        PropertyList(
                            Uid("5"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "2st Exam WAD"),
                            Description(ParameterList(Language("sq")), "Second season exam for WAD-1920v"),
                            Created(stampDate),
                            Categories(ParameterList(Language("en-GB")), "Exam"),
                            DtStart("20200701T100000Z"),
                            DtEnd("20200701T123000Z"),
                            RRule("FREQ=WEEKLY;BYDAY=WE")
                        )
                    )
                )
            )

        fun buildCalendarForClassSection(): Calendar {
            return Calendar(
                PropertyList(
                    ProdId("/v0/courses/$courseID/classes/$calTerm/$classSection"),
                    Version.VERSION_2_0
                ),
                ComponentList(
                    VEvent(
                        PropertyList(
                            Uid("6"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "WAD Monday Lecture"),
                            Description(
                                ParameterList(Language("sq")),
                                "Lectures of the WAD curricular unit for the 1718v-1D Class section"
                            ),
                            Created(stampDate),
                            Categories(ParameterList(Language("en-GB")), "Exam"),
                            DtStart("20200210T100000Z"),
                            DtEnd("20200210T123000Z"),
                            RRule("FREQ=WEEKLY;BYDAY=MO")
                        )
                    ),
                    VEvent(
                        PropertyList(
                            Uid("7"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "WAD Wednesday Lecture"),
                            Description(
                                ParameterList(Language("sq")),
                                "Lectures of the WAD curricular unit for the 1718v-1D Class section"
                            ),
                            Created(stampDate),
                            Categories(ParameterList(Language("en-GB")), "Exam"),
                            DtStart("20200212T100000Z"),
                            DtEnd("20200212T123000Z"),
                            RRule("FREQ=WEEKLY;BYDAY=WE")
                        )
                    )
                )
            )
        }

        fun buildComponentForClass(): Calendar =
            Calendar(
                PropertyList(
                    ProdId("/v0/courses/$courseID/classes/$calTerm"),
                    Version.VERSION_2_0
                ),
                ComponentList(
                    VToDo(
                        PropertyList(
                            Uid("2"),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "[WAD]: Assignment #1"),
                            Description(
                                ParameterList(Language("sq")),
                                "The first assignment. The goal is to implement an HTTP API..."
                            ),
                            Attach(URI("https://tools.ietf.org/html/rfc7231")),
                            Created(stampDate),
                            Due("20210419T235900Z"),
                            Categories(ParameterList(Language("pt-PT")), TextList(arrayOf("Exame", "Aula")))
                        )
                    )
                )
            )

        fun buildComponentForClassSection(): Calendar =
            Calendar(
                PropertyList(
                    ProdId("/v0/courses/$courseID/classes/$calTerm/$classSection"),
                    Version.VERSION_2_0
                ),
                ComponentList(
                    VEvent(
                        PropertyList(
                            Uid(classSectionComponentID),
                            DtStamp(stampDate),
                            Summary(ParameterList(Language("sq")), "WAD Monday Lecture"),
                            Description(
                                ParameterList(Language("sq")),
                                "Lectures of the WAD curricular unit for the 1718v-1D Class section"
                            ),
                            Created(stampDate),
                            Categories(ParameterList(Language("en-GB")), "Exam"),
                            DtStart("20200210T100000Z"),
                            DtEnd("20200210T123000Z"),
                            RRule("FREQ=WEEKLY;BYDAY=MO")
                        )
                    )
                )
            )

        //Adds parameters to a component property
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
        isValidSiren(Uri.forCalendarComponentByClass(courseID, calTerm, classComponentID))
    }

    @Test
    fun getCalendarComponentByClassSection() {
        isValidSiren(Uri.forCalendarComponentByClassSection(courseID, calTerm, classSection, classSectionComponentID))
    }


    /**
     * text/calendar content-type is generated from the
     * JSON object with the use of the calendar message converter
     */
    @Test
    fun getCalendarByClass_And_Compare() {
        val result = doGet(Uri.forCalendarByClass(courseID, calTerm))
        { accept = Media.MEDIA_TEXT_CALENDAR }
            .andReturn()
            .response
            .contentAsString

        /**
         * Had a small problem with the way the messageConverter
         * added newlines, the way obtained by the browser depending on the
         * oparating system and the
         * way multiline strings added new lines, for compatibility
         * in all situation the following regex removes all lines and spaces
         * from the strings.
         */
        val expected: String = calendarByClass.replace("\\s".toRegex(), "")
        val obtained: String = result.replace("\\s".toRegex(), "")
        Assertions.assertEquals(expected, obtained)
    }

    @Test
    fun getCalendarByClassSection_And_Compare() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
        { accept = Media.MEDIA_TEXT_CALENDAR }
            .andReturn()
            .response
            .contentAsString

        val expected: String = calendarByClassSection.replace("\\s".toRegex(), "")
        val obtained: String = result.replace("\\s".toRegex(), "")
        Assertions.assertEquals(expected, obtained)
    }


    /**
     * Builds ical4j calendar and compares with the core calendar
     */
    @Test
    fun checkIfValidCalClass() {
        //Get the core calendar representation
        val result = doGet(Uri.forCalendarByClass(courseID, calTerm))
        { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
            .response.contentAsString

        Assertions.assertEquals(classCalendarCal4j.output(), result)
    }

    @Test
    fun checkIfValidCalClassSection() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
        { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
            .response.contentAsString

        Assertions.assertEquals(classSectionCalendarCal4j.output(), result)
    }

    @Test
    fun checkIfValidComponentClass() {
        val result = doGet(Uri.forCalendarComponentByClass(courseID, calTerm, classComponentID))
        { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
            .response.contentAsString

        Assertions.assertEquals(classComponentCal4j.output(), result)
    }

    @Test
    fun checkIfValidComponentClassSection() {
        val result =
            doGet(Uri.forCalendarComponentByClassSection(courseID, calTerm, classSection, classSectionComponentID))
            { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
                .response.contentAsString

        Assertions.assertEquals(classSectionComponentCal4j.output(), result)
    }

}