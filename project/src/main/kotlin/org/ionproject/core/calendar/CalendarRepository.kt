package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.Event
import org.ionproject.core.calendar.icalendar.Todo
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.datetime.Duration
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.types.Date
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.Uri
import org.ionproject.core.calendar.icalendar.types.Duration as DurationType

class CalendarRepository {

    operator fun get(id: Int): Calendar? = calendars[id]

    private val calendars = HashMap<Int, Calendar>()

    init {
        val productIdentifier = "class/1"

        val eventUid = "event/1234"
        val summary = "Exame de DAW"
        val language = Language("pt/PT")
        val description = "Exame de Época normal de DAW"
        val stamp = DateTime.parse("20200226T143423Z")
        val categories = listOf("EXAM", "DAW", "EVALUATION", "NORMAL-SEASON")
        val start = DateTime.parse("20200620T140000Z")
        val duration = DurationType(hours = 2, minutes = 30)

        calendars[1] = Calendar(
            ProductIdentifier(productIdentifier),
            Version(),
            null,
            null,
            Event(
                UniqueIdentifier(eventUid),
                Summary(
                    summary,
                    language = language
                ),
                Description(description, language = language),
                DateTimeStamp(stamp),
                DateTimeCreated(stamp),
                Categories(categories, Language("en")),
                DateTimeStart(start),
                Duration(duration)
            ),
            Todo(
                UniqueIdentifier("todo/1324"),
                Summary("Primeira fase de exercícios de DAW", language = language),
                Description("Web API para suportar projetos, issues, labels, state e comments", language = language),
                Attachment(Uri("https://github.com/isel-leic-daw/1920v-public/wiki/phase-1)")),
                DateTimeStamp(DateTime.parse("20200302T100545Z")),
                DateTimeCreated(DateTime.parse("20200302T100545Z")),
                DateTimeDue(Date(2020, 4, 20)),
                Categories(listOf("DAW", "EVALUATION", "ASSIGNMENT"))
            )
        )
    }

}