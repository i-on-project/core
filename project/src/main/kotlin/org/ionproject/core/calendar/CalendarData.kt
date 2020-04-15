package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.Event
import org.ionproject.core.calendar.icalendar.Journal
import org.ionproject.core.calendar.icalendar.Todo
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeEnd
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.recurrence.RecurrenceRule
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.Recur
import org.ionproject.core.calendar.icalendar.types.Time
import org.ionproject.core.calendar.icalendar.types.WeekDay
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.util.*
import org.ionproject.core.calendar.icalendar.types.Date as DateType

object CalendarData {
    private const val CALENDAR_COMPONENT_ABBREVIATION = "cmp"
    private const val CALENDAR_COMPONENT = "dbo.CalendarComponent $CALENDAR_COMPONENT_ABBREVIATION"
    private const val CALENDAR_COMPONENT_ID = "$CALENDAR_COMPONENT_ABBREVIATION.id"
    private const val CALENDAR_COMPONENT_TYPE = "$CALENDAR_COMPONENT_ABBREVIATION.type"
    private const val CALENDAR_COMPONENT_CALENDAR_ID = "$CALENDAR_COMPONENT_ABBREVIATION.cid"
    private const val CALENDAR_COMPONENT_SUMMARY = "$CALENDAR_COMPONENT_ABBREVIATION.summary"
    private const val CALENDAR_COMPONENT_DESCRIPTION = "$CALENDAR_COMPONENT_ABBREVIATION.description"
    private const val CALENDAR_COMPONENT_DTSTART = "$CALENDAR_COMPONENT_ABBREVIATION.dtstart"
    private const val CALENDAR_COMPONENT_DTEND = "$CALENDAR_COMPONENT_ABBREVIATION.dtend"

    private const val CLASS_ABBREVIATION = "c"
    private const val CLASS = "dbo.Class $CLASS_ABBREVIATION"
    private const val CLASS_CALENDAR_ID = "$CLASS_ABBREVIATION.calendar"
    private const val CLASS_COURSE = "$CLASS_ABBREVIATION.courseId"
    private const val CLASS_TERM = "$CLASS_ABBREVIATION.term"

    private const val CLASS_SECTION_ABBREVIATION = "cs"
    private const val CLASS_SECTION = "dbo.Class $CLASS_SECTION_ABBREVIATION"
    private const val CLASS_SECTION_CALENDAR_ID = "$CLASS_SECTION_ABBREVIATION.calendar"
    private const val CLASS_SECTION_ID = "$CLASS_SECTION_ABBREVIATION.id"
    private const val CLASS_SECTION_COURSE = "$CLASS_SECTION_ABBREVIATION.courseId"
    private const val CLASS_SECTION_TERM = "$CLASS_SECTION_ABBREVIATION.term"

    private const val RRULE_ABBREVIATION = "rr"
    private const val RRULE = "dbo.RecurrenceRule $RRULE_ABBREVIATION"
    private const val RRULE_COMPONENT_ID = "$RRULE_ABBREVIATION.cid"
    private const val RRULE_FREQ = "$RRULE_ABBREVIATION.freq"
    private const val RRULE_BYDAY = "$RRULE_ABBREVIATION.byday"


    const val CALENDAR_FROM_CLASS_QUERY = """WITH calendar_id AS (
                    SELECT $CLASS_CALENDAR_ID
                    FROM $CLASS
                    WHERE $CLASS_COURSE :courseId AND $CLASS_TERM :term
                )
            SELECT $CALENDAR_COMPONENT_ID,
                    $CALENDAR_COMPONENT_TYPE,
                    $CALENDAR_COMPONENT_SUMMARY,
                    $CALENDAR_COMPONENT_DESCRIPTION,
                    $CALENDAR_COMPONENT_DTSTART,
                    $CALENDAR_COMPONENT_DTEND,
                    $RRULE_FREQ,
                    $RRULE_BYDAY
            FROM $CALENDAR_COMPONENT
            JOIN $RRULE ON $CALENDAR_COMPONENT_ID = $RRULE_COMPONENT_ID
            WHERE $CALENDAR_COMPONENT_CALENDAR_ID = calendar_id"""

    const val CALENDAR_FROM_CLASS_SECTION_QUERY =
        """WITH calendar_id AS (
                    SELECT $CLASS_SECTION_CALENDAR_ID
                    FROM $CLASS_SECTION
                    WHERE $CLASS_SECTION_COURSE :courseId AND $CLASS_SECTION_TERM :term AND $CLASS_SECTION_ID :classSectionId
                )
            SELECT $CALENDAR_COMPONENT_ID,
                    $CALENDAR_COMPONENT_TYPE,
                    $CALENDAR_COMPONENT_SUMMARY,
                    $CALENDAR_COMPONENT_DESCRIPTION,
                    $CALENDAR_COMPONENT_DTSTART,
                    $CALENDAR_COMPONENT_DTEND,
                    $RRULE_FREQ,
                    $RRULE_BYDAY
            FROM $CALENDAR_COMPONENT
            JOIN $RRULE ON $CALENDAR_COMPONENT_ID = $RRULE_COMPONENT_ID
            WHERE $CALENDAR_COMPONENT_CALENDAR_ID = calendar_id"""

    class CalendarComponentMapper : RowMapper<CalendarComponent> {
        override fun map(rs: ResultSet?, ctx: StatementContext?): CalendarComponent {
            if (rs != null) {
                val type = rs.getString(CALENDAR_COMPONENT_TYPE)
                val uid = UniqueIdentifier("$type/${rs.getInt(CALENDAR_COMPONENT_ID)}")
                val summary = Summary(rs.getString(CALENDAR_COMPONENT_SUMMARY))
                val description = Description(rs.getString(CALENDAR_COMPONENT_DESCRIPTION))
                val startDate = rs.getTimestamp(CALENDAR_COMPONENT_DTSTART).toDateTime()
                val dtStart = DateTimeStart(startDate)
                val dtStamp = DateTimeStamp(startDate)
                val created = DateTimeCreated(startDate)

                return when (type) {
                    "E" -> Event(
                        uid,
                        summary,
                        description,
                        dtStamp,
                        created,
                        null,
                        dtStart,
                        DateTimeEnd(rs.getTimestamp(CALENDAR_COMPONENT_DTSTART).toDateTime()),
                        RecurrenceRule(Recur(byDay = rs.getString(RRULE_BYDAY).split(",").map { WeekDay(WeekDay.Weekday.valueOf(it), null) }))
                    )
                    "J" -> Journal(
                        uid,
                        summary,
                        description,
                        null,
                        dtStamp,
                        dtStart,
                        created,
                        null
                    )
                    "T" -> Todo(
                        uid,
                        summary,
                        description,
                        null,
                        dtStamp,
                        created,
                        DateTimeDue(rs.getTimestamp(CALENDAR_COMPONENT_DTSTART).toDateTime()),
                        null
                    )
                    else -> throw UnknownCalendarComponentTypeException("The type represented by $type is not known.")
                }
            }
            throw IllegalArgumentException("Cannot map if result set is null.")
        }

    }
}

class UnknownCalendarComponentTypeException(message: String) : Exception(message)

private fun java.util.Date.toDateTime(): DateTime {
    val calendar = Calendar.Builder().setInstant(time).build()
    return DateTime(
        DateType(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ),
        Time(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
    )
}


