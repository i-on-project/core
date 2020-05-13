package org.ionproject.core.calendar


import net.fortuna.ical4j.model.*
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VJournal
import net.fortuna.ical4j.model.component.VToDo
import net.fortuna.ical4j.model.parameter.Language
import net.fortuna.ical4j.model.property.*
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.net.URI


internal class CalendarControllerTest : ControllerTester() {
    companion object {
        val courseID = 1
        val calTerm = "1718v"
        val classSection = "1D"
        val componentID = "1"

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
                """

        val calendarByClassSection =
                """BEGIN:VCALENDAR
                PRODID:/v0/courses/1/classes/1718v/1D
                VERSION:2.0
                END:VCALENDAR"""

        val classCalendarCal4j = buildCalendarForClass()

        /**
         * Builds a cal4j calendar with the data defined by the Read API
         * Cal Mock Data for comparison with the one generated
         * by the core calendar.
         *
         * https://github.com/ical4j/ical4j
         */
        fun buildCalendarForClass() : Calendar {
            val calendar = Calendar()   //ical4j calendar (not the one implemented by the group)
            calendar.getProperties().add(ProdId("/v0/courses/1/classes/1718v"))
            calendar.getProperties().add(Version.VERSION_2_0)

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

            val journal =  VJournal(journalProperties)

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
                            Summary(buildParameterList(Language("en-US")),"1st exam WAD") ,
                            Description(buildParameterList(Language("en-US")), "Normal season exam for WAD-1920v"),
                            Created("20200511T162630Z"),
                            Categories(buildParameterList(Language("en-GB")), "EXAM"),
                            DtStart("20200619T140000Z"),
                            DtEnd("20200619T150000Z")
                    )
            )

            val event = VEvent(eventProperties)

            //Add the components to the calendar
            calendar.getComponents().addAll(listOf(journal, todo1, todo2, event))

            print(event.getProperties())
            return calendar
        }

        //Adds parameters to a component property
        fun buildParameterList(param: Parameter) : ParameterList {
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
        isValidSiren(Uri.forCalendarComponentByClass(courseID, calTerm, componentID))
    }

//    @Test NO DATA FOR THIS TEST (yet)
//    fun getCalendarComponentByClassSection() {
//        isValidSiren(Uri.forCalendarComponentByClassSection(courseID, calTerm, classSection, componentID))
//    }



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

        Assertions.assertEquals(calendarByClass.replace("\\s".toRegex(), ""), result)
    }

    @Test
    fun getCalendarByClassSection_And_Compare() {
        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
            {accept = Media.MEDIA_TEXT_CALENDAR}
                .andReturn()
                .response
                .contentAsString
                .replace("\\s".toRegex(), "")

        Assertions.assertEquals(calendarByClassSection.replace("\\s".toRegex(), ""), result)
    }




    /**
     * Builds ical4j calendar and compares with the core calendar
     */
    @Test
    fun checkIfValidCalClass() {
        //Get the core calendar representation
        val result = doGet(Uri.forCalendarByClass(courseID, calTerm))
        {accept = Media.MEDIA_TEXT_CALENDAR}.
                andReturn()
                .response.contentAsString

        Assertions.assertEquals(classCalendarCal4j.toString(), result)
    }

    @Test
    fun checkIfValidCalClassSection() {
        val calendar = Calendar()   //ical4j calendar (not the one implemented by the group)
        calendar.getProperties().add(ProdId("/v0/courses/1/classes/1718v/1D"))
        calendar.getProperties().add(Version.VERSION_2_0)

        val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
        {accept = Media.MEDIA_TEXT_CALENDAR}.
                andReturn()
                .response.contentAsString

        Assertions.assertEquals(calendar.toString(), result)
    }

    @Test
    fun checkIfValidComponentClass() {
        val calendar = Calendar()
        calendar.getProperties().addAll(
                listOf(
                        ProdId("/v0/courses/1/classes/1718v"),
                        Version.VERSION_2_0
                )
        )

        val properties = PropertyList<Property>()
        properties.addAll(
                listOf(
                        Uid("1"),
                        DtStamp("20200511T162630Z"),
                        Summary(buildParameterList(Language("pt-PT")), "uma sinopse"),
                        Summary(buildParameterList(Language("en-US")), "some summary"),
                        Description(buildParameterList(Language("en-US")), "this is a description"),
                        Attach(URI("https://www.google.com")),
                        DtStart("20200410T140000Z"),
                        Created("20200511T162630Z"),
                        Categories(buildParameterList(Language("pt-PT")),"EXAME"),
                        Categories(buildParameterList(Language("en-US")),"EXAM"),
                        Categories(buildParameterList(Language("en-GB")),"EXAM")
                )
        )
        calendar.getComponents().add(VJournal(properties))

        val result = doGet(Uri.forCalendarComponentByClass(courseID, calTerm, "1"))
        {accept = Media.MEDIA_TEXT_CALENDAR}.
                andReturn()
                .response.contentAsString

        Assertions.assertEquals(calendar.toString(), result)
    }

//    @Test
//    fun checkIfValidComponentClassSection() {
//        //Not one in the db for now
//    }

}