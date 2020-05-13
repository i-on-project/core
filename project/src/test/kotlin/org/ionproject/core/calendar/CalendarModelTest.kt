package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.*
import org.ionproject.core.calendar.icalendar.properties.calendar.CalendarScale
import org.ionproject.core.calendar.icalendar.properties.calendar.Method
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeEnd
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.Time
import org.ionproject.core.calendar.icalendar.types.Uri
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class CalendarModelTest {
    @Test
    fun createCalendar() {
        val calendar: Calendar =
                Calendar(
                        ProductIdentifier("product"),
                        Version("version"),
                        CalendarScale("GREGORIAN"),
                        Method("METHOD"),
                        mutableListOf()
                )

        Assertions.assertEquals(calendar.prod.value, "product")
        Assertions.assertEquals(calendar.version.value, "version")
        Assertions.assertEquals(calendar.scale?.value, "GREGORIAN")
        Assertions.assertEquals(calendar.method?.value, "METHOD")
        Assertions.assertEquals(calendar.components, mutableListOf<CalendarComponent>())
    }

    @Test
    fun createTodo() {
        val todo: Todo =
                Todo(
                        UniqueIdentifier("uid"),
                        arrayOf<Summary>(Summary("summary")),
                        arrayOf<Description>(Description("description")),
                        arrayOf<Attachment>(Attachment(Uri("something"))),
                        DateTimeStamp(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        DateTimeCreated(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        DateTimeDue(Date.of(2020, 0, 31)),
                        arrayOf<Categories>(Categories("categorie"))
                )

        Assertions.assertEquals(todo.uid.value, "uid")
        Assertions.assertEquals(todo.componentName, "VTODO")
        Assertions.assertEquals(todo.dtStamp.value.value,
                DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1)).toString())
    }

    @Test
    fun createJournal() {
        val journal: Journal =
                Journal(
                        UniqueIdentifier("uid"),
                        arrayOf<Summary>(Summary("summary")),
                        arrayOf<Description>(Description("description")),
                        arrayOf<Attachment>(Attachment(Uri("something"))),
                        DateTimeStamp(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        DateTimeStart(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        DateTimeCreated(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        arrayOf<Categories>(Categories("categorie"))
                )

        Assertions.assertEquals(journal.uid.value, "uid")
        Assertions.assertEquals(journal.componentName, "VJOURNAL")
        Assertions.assertEquals(journal.dtStamp.value.value,
                DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1)).toString())
    }

    @Test
    fun createEvent() {
        val event: Event =
                Event(
                        UniqueIdentifier("uid"),
                        arrayOf<Summary>(Summary("summary")),
                        arrayOf<Description>(Description("description")),
                        DateTimeStamp(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        DateTimeCreated(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        arrayOf<Categories>(Categories("categorie")),
                        DateTimeStart(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        DateTimeEnd(DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1))),
                        null
                )

        Assertions.assertEquals(event.uid.value, "uid")
        Assertions.assertEquals(event.componentName, "VEVENT")
        Assertions.assertEquals(event.dtStamp.value.value,
                DateTime(Date.of(2020, 0, 31), Time.of(1, 1, 1)).toString())

    }
}