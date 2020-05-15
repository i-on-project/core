package org.ionproject.core.calendar


import net.fortuna.ical4j.model.*
import org.ionproject.core.utils.ParameterList
import org.ionproject.core.utils.ComponentList
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VJournal
import net.fortuna.ical4j.model.component.VToDo
import net.fortuna.ical4j.model.parameter.Language
import net.fortuna.ical4j.model.property.*
import org.ionproject.core.calendar.icalendar.Journal
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.PropertyList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import java.net.URI


internal class CalendarControllerTest : ControllerTester() {
    companion object {
        val courseID = 2
        val calTerm = "1718v"
        val classSection = "1D"
        val componentID = "1"

        val calendarByClass = """
"""

        val calendarByClassSection = """BEGIN:VCALENDAR
PRODID:/v0/courses/2/classes/1718v/1D
VERSION:2.0
BEGIN:VEVENT
UID:5
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:WAD Monday Lecture
DESCRIPTION;LANGUAGE=en-US:Lectures of the WAD curricular unit for the 1718
 v-1D Class section
CREATED:20200101T163530Z
CATEGORIES;LANGUAGE=en-GB:Exam
DTSTART:20200210T100000Z
DTEND:20200210T123000Z
RRULE:FREQ=WEEKLY;BYDAY=MO
END:VEVENT
BEGIN:VEVENT
UID:6
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:WAD Wednesday Lecture
DESCRIPTION;LANGUAGE=en-US:Lectures of the WAD curricular unit for the 1718
 v-1D Class section
CREATED:20200101T163530Z
CATEGORIES;LANGUAGE=en-GB:Exam
DTSTART:20200212T100000Z
DTEND:20200212T123000Z
RRULE:FREQ=WEEKLY;BYDAY=WE
END:VEVENT
END:VCALENDAR"""

        val classCalendarCal4j = buildCalendarForClass()
        val classSectionCalendarCal4j = buildCalendarForClassSection()

        /**
         * Builds a cal4j calendar with the data defined by the Read API
         * Cal Mock Data for comparison with the one generated
         * by the core calendar.
         *
         * https://github.com/ical4j/ical4j
         */
        private fun buildCalendarForClass(): Calendar {
            val calendar = Calendar()   //ical4j calendar (not the one implemented by the group)
            calendar.properties.add(ProdId("/v0/courses/1/classes/1718v"))
            calendar.properties.add(Version.VERSION_2_0)

            //Build Journal Component
            val journalProperties = PropertyList<Property>()
            journalProperties.addAll(
                    listOf(
                            Uid("1"),
                            DtStamp("20200511T162630Z"),
                            Summary(buildParameterList(Language("pt-PT")), "uma sinopse"),
                            Summary(buildParameterList(Language("en-US")), "some summary"),
                            Description(buildParameterList(Language("en-US")), "this is a description"),
                            Attach(URI("https://www.google.com")),
                            DtStart("20200410T140000Z"),
                            Created("20200511T162630Z"),
                            Categories(buildParameterList(Language("pt-PT")), "EXAME"),
                            Categories(buildParameterList(Language("en-US")), "EXAM"),
                            Categories(buildParameterList(Language("en-GB")), "EXAM")
                    )
            )

            val journal = VJournal(journalProperties)

            //Build ToDo Component
            val todo1Properties = PropertyList<Property>()
            todo1Properties.addAll(
                    listOf(
                            Uid("2"),
                            DtStamp("20200511T162630Z"),
                            Summary(buildParameterList(Language("en-US")), "do the thing"),
                            Description(buildParameterList(Language("en-US")), "I have to do the thing soon enough"),
                            Attach(URI("https://www.google.com")),
                            Created("20200511T162630Z"),
                            Due("20210619T140000Z"),
                            Categories(buildParameterList(Language("pt-PT")), "EXAME"),
                            Categories(buildParameterList(Language("en-US")), "EXAM")
                    )
            )
            val todo1 = VToDo(todo1Properties)

            //Build ToDo Component
            val todo2Properties = PropertyList<Property>()
            todo2Properties.addAll(
                    listOf(
                            Uid("3"),
                            DtStamp("20200511T162630Z"),
                            Summary(buildParameterList(Language("en-US")), "don't do the thing"),
                            Description(buildParameterList(Language("en-US")), "I have to do the thing soon enough"),
                            Created("20200511T162630Z"),
                            Due("20210619T140000Z"),
                            Categories(buildParameterList(Language("pt-PT")), "EXAME"),
                            Categories(buildParameterList(Language("en-US")), "EXAM")

                    )
            )

            val todo2 = VToDo(todo2Properties)

            //Build Event Component
            val eventProperties = PropertyList<Property>()
            eventProperties.addAll(
                    listOf(
                            Uid("4"),
                            DtStamp("20200511T162630Z"),
                            Summary(buildParameterList(Language("en-US")), "1st exam WAD"),
                            Description(buildParameterList(Language("en-US")), "Normal season exam for WAD-1920v"),
                            Created("20200511T162630Z"),
                            Categories(buildParameterList(Language("en-GB")), "EXAM"),
                            DtStart("20200619T140000Z"),
                            DtEnd("20200619T150000Z")
                    )
            )

            val event = VEvent(eventProperties)

            //Add the components to the calendar
            calendar.components.addAll(listOf(journal, todo1, todo2, event))

            print(event.properties)
            return calendar
        }

        fun buildCalendarForClassSection(): Calendar {
            return Calendar(
                PropertyList(
                    ProdId("/v0/courses/1/classes/1718v/1D"),
                    Version.VERSION_2_0
                ),
                ComponentList(
                    VJournal(
                        PropertyList(
                            Uid("5"),
                            DtStamp("20200511T162630Z"),
                            Summary(ParameterList(Language("pt-PT")), "uma sinopse"),
                            Description(ParameterList(Language("en-US")), "this is a description"),
                            Attach(URI("https://www.example.com")),
                            DtStart("20200514T121300Z"),
                            Created("20200511T162630Z"),
                            Categories(
                                ParameterList(Language("pt-PT")),
                                "EXAME"
                            ),
                            Categories(
                                ParameterList(Language("en-US")),
                                "EXAM"
                            ),
                            Categories(
                                ParameterList(Language("en-GB")),
                                "EXAM"
                            )
                        )
                    )
                )
            )
        }

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
        mocker.get(Uri.forCalendarComponentByClass(courseID, calTerm, componentID)) {
            accept = Media.MEDIA_SIREN
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
        }
    }

    @Test
    fun getCalendarComponentByClassSection() {
        mocker.get(Uri.forCalendarComponentByClassSection(courseID, calTerm, classSection, "5")) {
            accept = Media.MEDIA_SIREN
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

        Assertions.assertEquals(classCalendarCal4j.toString(), result)
    }

    @Test
    fun checkIfValidCalClassSection() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
        { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
                .response.contentAsString

        Assertions.assertEquals(classSectionCalendarCal4j.toString(), result)
    }

    @Test
    fun checkIfValidComponentClass() {
        val calendar = Calendar()
        calendar.properties.addAll(
                listOf(
                        ProdId("/v0/courses/1/classes/1718v"),
                        Version.VERSION_2_0
                )
        )

        val properties = PropertyList(
            Uid("1"),
            DtStamp("20200511T162630Z"),
            Summary(buildParameterList(Language("pt-PT")), "uma sinopse"),
            Summary(buildParameterList(Language("en-US")), "some summary"),
            Description(buildParameterList(Language("en-US")), "this is a description"),
            Attach(URI("https://www.google.com")),
            DtStart("20200410T140000Z"),
            Created("20200511T162630Z"),
            Categories(buildParameterList(Language("pt-PT")), "EXAME"),
            Categories(buildParameterList(Language("en-US")), "EXAM"),
            Categories(buildParameterList(Language("en-GB")), "EXAM")
        )

        calendar.components.add(VJournal(properties))

        val result = doGet(Uri.forCalendarComponentByClass(courseID, calTerm, "1"))
        { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
                .response.contentAsString

        Assertions.assertEquals(calendar.toString(), result)
    }

//    @Test
//    fun checkIfValidComponentClassSection() {
//        //Not one in the db for now
//    }

}