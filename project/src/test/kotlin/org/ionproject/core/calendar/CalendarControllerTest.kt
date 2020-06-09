package org.ionproject.core.calendar

import net.fortuna.ical4j.model.*
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
import org.ionproject.core.utils.ParameterList
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

      val calendarByClass = """BEGIN:VCALENDAR
PRODID:/v0/courses/2/classes/1718v
VERSION:2.0
BEGIN:VTODO
UID:1
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:[WAD]: Assignment #1
DESCRIPTION;LANGUAGE=en-US:The first assignment. The goal is to implement a
 n HTTP API...
ATTACH:https://tools.ietf.org/html/rfc7231
CREATED:20200101T163530Z
DUE:20210419T235900Z
CATEGORIES;LANGUAGE=pt-PT:Exame,Aula
END:VTODO
BEGIN:VTODO
UID:2
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:[WAD]: Assignment #2
DESCRIPTION;LANGUAGE=en-US:The second assignment. The goal is to implement 
 a Web Client for the API...
CREATED:20200101T163530Z
DUE:20210612T235900Z
CATEGORIES;LANGUAGE=pt-PT:Exame,Aula
END:VTODO
BEGIN:VEVENT
UID:3
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:1st Exam WAD
DESCRIPTION;LANGUAGE=en-US:Normal season exam for WAD-1718v
CREATED:20200101T163530Z
CATEGORIES;LANGUAGE=en-US:Exam
DTSTART:20200619T180000Z
DTEND:20200619T193000Z
END:VEVENT
BEGIN:VEVENT
UID:4
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:2nd Exam WAD
DESCRIPTION;LANGUAGE=en-US:Second season exam for WAD-1718v
CREATED:20200101T163530Z
CATEGORIES;LANGUAGE=en-US:Exam
DTSTART:20200701T100000Z
DTEND:20200701T123000Z
END:VEVENT
END:VCALENDAR"""

      val calendarByClassSection = """BEGIN:VCALENDAR
PRODID:/v0/courses/2/classes/1718v/1D
VERSION:2.0
BEGIN:VEVENT
UID:5
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:WAD Monday Lecture
DESCRIPTION;LANGUAGE=en-US:Lectures of the WAD curricular unit\, for the 1718
 v-1D Class section
CREATED:20200101T163530Z
CATEGORIES;LANGUAGE=en-US:Lecture
DTSTART:20200210T100000Z
DTEND:20200210T123000Z
RRULE:FREQ=WEEKLY;UNTIL=20200612T235000Z;BYDAY=MO
END:VEVENT
BEGIN:VEVENT
UID:6
DTSTAMP:20200101T163530Z
SUMMARY;LANGUAGE=en-US:WAD Wednesday Lecture
DESCRIPTION;LANGUAGE=en-US:Lectures of the WAD curricular unit\, for the 1718
 v-1D Class section
CREATED:20200101T163530Z
CATEGORIES;LANGUAGE=en-US:Lecture
DTSTART:20200212T100000Z
DTEND:20200212T123000Z
RRULE:FREQ=WEEKLY;UNTIL=20200612T235000Z;BYDAY=WE
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
        calendar.properties.add(ProdId("/v0/courses/$courseID/classes/$calTerm"))
        calendar.properties.add(Version.VERSION_2_0)

        val enLang = buildParameterList(Language("en-US"))
        val ptLang = buildParameterList(Language("pt-PT"))
        val rfcAttachment = URI.create("https://tools.ietf.org/html/rfc7231")

        val todoWad1Assignment = VToDo(PropertyList<Property>().fluentAdd(
          Uid("1"),
          DtStamp("20200101T163530Z"),
          Summary(enLang, "[WAD]: Assignment #1"),
          Description(enLang, "The first assignment. The goal is to implement an HTTP API..."),
          Attach(rfcAttachment),
          Created("20200101T163530Z"),
          Due("20210419T235900Z"),
          Categories(ptLang, "Exame,Aula")
        ) as PropertyList<Property>)

        val todoWad2Assignment = VToDo(PropertyList<Property>().fluentAdd(
          Uid("2"),
          DtStamp("20200101T163530Z"),
          Summary(enLang, "[WAD]: Assignment #2"),
          Description(enLang, "The second assignment. The goal is to implement a Web Client for the API..."),
          Created("20200101T163530Z"),
          Due("20210612T235900Z"),
          Categories(ptLang, "Exame,Aula")
        ) as PropertyList<Property>)

        val eventWad1Exam = VEvent(PropertyList<Property>().fluentAdd(
          Uid("3"),
          DtStamp("20200101T163530Z"),
          Summary(enLang, "1st Exam WAD"),
          Description(enLang, "Normal season exam for WAD-1718v"),
          Created("20200101T163530Z"),
          Categories(enLang, "Exam"),
          DtStart("20200619T180000Z"),
          DtEnd("20200619T193000Z")
        ) as PropertyList<Property>)

        val eventWad2Exam = VEvent(PropertyList<Property>().fluentAdd(
          Uid("4"),
          DtStamp("20200101T163530Z"),
          Summary(enLang, "2nd Exam WAD"),
          Description(enLang, "Second season exam for WAD-1718v"),
          Created("20200101T163530Z"),
          Categories(enLang, "Exam"),
          DtStart("20200701T100000Z"),
          DtEnd("20200701T123000Z")
        ) as PropertyList<Property>)

        calendar.components.fluentAdd(
          todoWad1Assignment,
          todoWad2Assignment,
          eventWad1Exam,
          eventWad2Exam)

        return calendar
      }

        fun buildCalendarForClassSection(): Calendar {
          val calendar = Calendar()
          calendar.properties.add(ProdId("/v0/courses/$courseID/classes/$calTerm/$classSection"))
          calendar.properties.add(Version.VERSION_2_0)

          val enLang = buildParameterList(Language("en-US"))
          val eventWadLectureMonday = VEvent(PropertyList<Property>().fluentAdd(
            Uid("5"),
            DtStamp("20200101T163530Z"),
            Summary(enLang, "WAD Monday Lecture"),
            Description(enLang, "Lectures of the WAD curricular unit, for the 1718v-1D Class section"),
            Created("20200101T163530Z"),
            Categories(enLang, "Lecture"),
            DtStart("20200210T100000Z"),
            DtEnd("20200210T123000Z"),
            RRule("FREQ=WEEKLY;UNTIL=20200612T235000Z;BYDAY=MO")
          ) as PropertyList<Property>)

          val eventWadLectureWednesday = VEvent(PropertyList<Property>().fluentAdd(
            Uid("6"),
            DtStamp("20200101T163530Z"),
            Summary(enLang, "WAD Wednesday Lecture"),
            Description(enLang, "Lectures of the WAD curricular unit, for the 1718v-1D Class section"),
            Created("20200101T163530Z"),
            Categories(enLang, "Lecture"),
            DtStart("20200212T100000Z"),
            DtEnd("20200212T123000Z"),
            RRule("FREQ=WEEKLY;UNTIL=20200612T235000Z;BYDAY=WE")
          ) as PropertyList<Property>)

          calendar.components.fluentAdd(
            eventWadLectureMonday,
            eventWadLectureWednesday)

          return calendar
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
      val expected: String = calendarByClass.removeWhitespace()
      val obtained: String = result.removeWhitespace()
      Assertions.assertEquals(expected, obtained)
    }

    @Test
    fun getCalendarByClassSection_And_Compare() {
      val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
      { accept = Media.MEDIA_TEXT_CALENDAR }
        .andReturn()
        .response
        .contentAsString

      val expected: String = calendarByClassSection.removeWhitespace()
      val obtained: String = result.removeWhitespace()
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

      Assertions.assertEquals(
        classCalendarCal4j.toString().removeWhitespace(),
        result.removeWhitespace())
    }

    @Test
    fun checkIfValidCalClassSection() {
      val result = doGet(Uri.forCalendarByClassSection(courseID, calTerm, classSection))
      { accept = Media.MEDIA_TEXT_CALENDAR }.andReturn()
        .response.contentAsString

      Assertions.assertEquals(
        classSectionCalendarCal4j.toString().removeWhitespace(),
        result.removeWhitespace())
    }
}
