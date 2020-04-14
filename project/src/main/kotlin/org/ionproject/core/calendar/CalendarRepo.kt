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
import org.ionproject.core.calendar.icalendar.properties.components.recurrence.RecurrenceRule
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.types.*
import org.springframework.stereotype.Repository
import org.ionproject.core.calendar.icalendar.types.Duration as DurationType

interface CalendarRepo {
    fun getClassCalendar(courseId: Int, calendarTerm: String): Calendar
    fun getClassSectionCalendar(courseId: Int, calendarTerm: String, classSectionId: Int): Calendar
}

@Repository
class CalendarRepoImpl : CalendarRepo {
    private val language = Language("pt/PT")

    override fun getClassCalendar(courseId: Int, calendarTerm: String): Calendar = Calendar(
        ProductIdentifier("class/1"),
        Version(),
        null,
        null,
        mutableListOf(
            Event(
                UniqueIdentifier("event/1234"),
                Summary(
                    "Exame de DAW",
                    language = language
                ),
                Description("Exame de Época normal de DAW", language = language),
                DateTimeStamp(DateTime.parse("20200226T143423Z")),
                DateTimeCreated(DateTime.parse("20200226T143423Z")),
                Categories("EXAM", "DAW", "EVALUATION", "NORMAL-SEASON", language = Language("en")),
                DateTimeStart(DateTime.parse("20200620T140000Z")),
                Duration(DurationType(hours = 2, minutes = 30))
            ),
            Todo(
                UniqueIdentifier("todo/1324"),
                Summary("Primeira fase de exercícios de DAW", language = language),
                Description("Web API para suportar projetos, issues, labels, state e comments", language = language),
                Attachment(Uri("https://github.com/isel-leic-daw/1920v-public/wiki/phase-1")),
                DateTimeStamp(DateTime.parse("20200302T100545Z")),
                DateTimeCreated(DateTime.parse("20200302T100545Z")),
                DateTimeDue(Date(2020, 4, 20)),
                Categories("DAW", "EVALUATION", "ASSIGNMENT")
            )
        )

    )

    override fun getClassSectionCalendar(courseId: Int, calendarTerm: String, classSectionId: Int): Calendar =
        Calendar(
            ProductIdentifier("course/1/class/1/1"),
            components = mutableListOf(
                Event(
                    UniqueIdentifier("event/1235"),
                    Summary(
                        "Aula de DAW",
                        language = language
                    ),
                    Description("Aula de 2ª feira de DAW(1h30)", language = language),
                    DateTimeStamp(DateTime.parse("20200226T143423Z")),
                    DateTimeCreated(DateTime.parse("20200226T143423Z")),
                    Categories("LECTURE", "DAW", language = Language("en")),
                    DateTimeStart(DateTime.parse("20200226T113000Z")),
                    Duration(DurationType(hours = 1, minutes = 30)),
                    RecurrenceRule(Recur(DateTime.parse("20200613T000000Z"), byDay = listOf(WeekDay(WeekDay.Weekday.MO))))
                )
            )
        )

}
