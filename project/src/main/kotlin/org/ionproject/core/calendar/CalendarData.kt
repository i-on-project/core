package org.ionproject.core.calendar

import org.ionproject.core.calendar.category.CategoryRepo
import org.ionproject.core.calendar.icalendar.*
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeEnd
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.recurrence.RecurrenceRule
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.calendar.icalendar.types.*
import org.ionproject.core.calendar.language.LanguageRepo
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.net.URI
import java.sql.ResultSet
import java.time.OffsetDateTime
import org.ionproject.core.calendar.icalendar.types.Date as DateType

object CalendarData {
    private const val CALENDAR_COMPONENT_ABBREVIATION = "cmp"
    private const val CALENDAR_COMPONENT = "dbo.v_Components $CALENDAR_COMPONENT_ABBREVIATION"

    private const val UID_COLUMN = "uid"
    private const val CALENDARS_COLUMN = "calendars"
    private const val TYPE_COLUMN = "type"
    private const val SUMMARIES_COLUMN = "summaries"
    private const val SUMMARIES_LANGUAGE_COLUMN = "summaries_language"
    private const val DESCRIPTIONS_COLUMN = "descriptions"
    private const val DESCRIPTIONS_LANGUAGE_COLUMN = "descriptions_language"
    private const val CATEGORIES_COLUMN = "categories"
    private const val DTSTAMP_COLUMN = "dtstamp"
    private const val CREATED_COLUMN = "created"
    private const val ATTACHMENTS_COLUMN = "attachments"
    private const val DTSTART_COLUMN = "dtstart"
    private const val DTEND_COLUMN = "dtend"
    private const val BYDAY_COLUMN = "byday"
    private const val DUE_COLUMN = "due"

    private const val UID = "$CALENDAR_COMPONENT_ABBREVIATION.$UID_COLUMN"
    private const val CALENDARS = "$CALENDAR_COMPONENT_ABBREVIATION.$CALENDARS_COLUMN"
    private const val TYPE = "$CALENDAR_COMPONENT_ABBREVIATION.$TYPE_COLUMN"
    private const val SUMMARIES = "$CALENDAR_COMPONENT_ABBREVIATION.$SUMMARIES_COLUMN"
    private const val SUMMARIES_LANGUAGE = "$CALENDAR_COMPONENT_ABBREVIATION.$SUMMARIES_LANGUAGE_COLUMN"
    private const val DESCRIPTIONS = "$CALENDAR_COMPONENT_ABBREVIATION.$DESCRIPTIONS_COLUMN"
    private const val DESCRIPTIONS_LANGUAGE = "$CALENDAR_COMPONENT_ABBREVIATION.$DESCRIPTIONS_LANGUAGE_COLUMN"
    private const val CATEGORIES = "$CALENDAR_COMPONENT_ABBREVIATION.$CATEGORIES_COLUMN"
    private const val DTSTAMP = "$CALENDAR_COMPONENT_ABBREVIATION.$DTSTAMP_COLUMN"
    private const val CREATED = "$CALENDAR_COMPONENT_ABBREVIATION.$CREATED_COLUMN"
    private const val ATTACHMENTS = "$CALENDAR_COMPONENT_ABBREVIATION.$ATTACHMENTS_COLUMN"
    private const val DTSTART = "$CALENDAR_COMPONENT_ABBREVIATION.$DTSTART_COLUMN"
    private const val DTEND = "$CALENDAR_COMPONENT_ABBREVIATION.$DTEND_COLUMN"
    private const val BYDAY = "$CALENDAR_COMPONENT_ABBREVIATION.$BYDAY_COLUMN"
    private const val DUE = "$CALENDAR_COMPONENT_ABBREVIATION.$DUE_COLUMN"

    private const val SELECT = """
        SELECT 
            $UID,
            $TYPE,
            ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT($SUMMARIES_LANGUAGE, $SUMMARIES)) AS $SUMMARIES_COLUMN,
            ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT($DESCRIPTIONS_LANGUAGE, $DESCRIPTIONS)) AS $DESCRIPTIONS_COLUMN,
            ARRAY_AGG(DISTINCT $CATEGORIES) AS $CATEGORIES_COLUMN,
            ARRAY_AGG(DISTINCT $ATTACHMENTS) AS $ATTACHMENTS_COLUMN,
            $DTSTAMP,
            $CREATED
            $DTSTART,
            $DTEND,
            $DUE,
            $BYDAY
    """

    private const val GROUP_BY = """
        GROUP BY
            $UID
    """

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

    const val CALENDAR_FROM_CLASS_QUERY = """WITH calendar_id AS (
                    SELECT $CLASS_CALENDAR_ID
                    FROM $CLASS
                    WHERE $CLASS_COURSE = :courseId AND $CLASS_TERM = :term
                )
            $SELECT
            FROM
                $CALENDAR_COMPONENT
            $GROUP_BY
            WHERE $CALENDARS = (SELECT * FROM calendar_id)"""

    const val CALENDAR_COMPONENT_FROM_CLASS_QUERY = """WITH calendar_id AS (
                    SELECT $CLASS_CALENDAR_ID
                    FROM $CLASS
                    WHERE $CLASS_COURSE = :courseId AND $CLASS_TERM = :term
                )
            $SELECT
            FROM $CALENDAR_COMPONENT
            $GROUP_BY
            WHERE $CALENDARS = (SELECT * FROM calendar_id) AND $UID = :componentId"""

    const val CALENDAR_FROM_CLASS_SECTION_QUERY =
        """WITH calendar_id AS (
                    SELECT $CLASS_SECTION_CALENDAR_ID
                    FROM $CLASS_SECTION
                    WHERE $CLASS_SECTION_COURSE = :courseId AND $CLASS_SECTION_TERM = :term AND $CLASS_SECTION_ID = :classSectionId
                )
            $SELECT
            FROM $CALENDAR_COMPONENT
            $GROUP_BY
            WHERE $CALENDARS = (SELECT * FROM calendar_id)"""

    const val CALENDAR_COMPONENT_FROM_CLASS_SECTION_QUERY = """WITH calendar_id AS (
                    SELECT $CLASS_SECTION_CALENDAR_ID
                    FROM $CLASS_SECTION
                    WHERE $CLASS_SECTION_COURSE = :courseId AND $CLASS_SECTION_TERM = :term AND $CLASS_SECTION_ID = :classSectionId
                )
            $SELECT
            FROM $CALENDAR_COMPONENT
            $GROUP_BY
            WHERE $CALENDARS = (SELECT * FROM calendar_id) AND $UID = :componentId"""

    @Component
    class CalendarComponentMapper(
        private val languageRepo: LanguageRepo,
        private val categoryRepo: CategoryRepo
    ) : RowMapper<CalendarComponent> {

        override fun map(rs: ResultSet, ctx: StatementContext): CalendarComponent {
            val type = rs.getString(TYPE)
            val uid = UniqueIdentifier(rs.getInt(UID).toString(16)) // TODO(use constant for uid radix)
            val categories = rs.getCategories(CATEGORIES_COLUMN)
            val summary = rs.getSummaries(SUMMARIES_COLUMN)
            val description = rs.getDescriptions(DESCRIPTIONS_COLUMN)
            val dtStart = DateTimeStart(rs.getDatetime(DTSTART_COLUMN))
            val dtStamp = DateTimeStamp(rs.getDatetime(DTSTAMP_COLUMN))
            val created = DateTimeCreated(rs.getDatetime(CREATED_COLUMN))

            return when (type) {
                "E" -> Event(
                    uid,
                    summary,
                    description,
                    dtStamp,
                    created,
                    categories,
                    dtStart,
                    DateTimeEnd(rs.getDatetime(DTSTART)),
                    RecurrenceRule(Recur(byDay = rs.getString(BYDAY_COLUMN).split(",").map { WeekDay(WeekDay.Weekday.valueOf(it), null) }))
                )
                "J" -> Journal(
                    uid,
                    summary,
                    description,
                    rs.getAttachments(ATTACHMENTS_COLUMN),
                    dtStamp,
                    dtStart,
                    created,
                    categories
                )
                "T" -> Todo(
                    uid,
                    summary,
                    description,
                    rs.getAttachments(ATTACHMENTS_COLUMN),
                    dtStamp,
                    created,
                    DateTimeDue(rs.getDatetime(DTSTART)),
                    categories
                )
                else -> throw UnknownCalendarComponentTypeException("The type represented by $type is not known.")
            }
        }

        private fun ResultSet.getSummaries(columnName: String) : Array<Summary> {
            val summaries = getArray(columnName).array as Array<String>

            return summaries.map { summary ->
                val text = summary.substringAfter(':')
                val language = summary.substringBefore(':').toInt()

                Summary(
                    text,
                    language = languageRepo.byId(language)
                )
            }.toTypedArray()
        }

        private fun ResultSet.getDescriptions(columnName: String) : Array<Description> {
            val descriptions = getArray(columnName).array as Array<String>

            return descriptions.map { description ->
                val text = description.substringAfter(':')
                val language = description.substringBefore(':').toInt()

                Description(
                    text,
                    language = languageRepo.byId(language)
                )
            }.toTypedArray()
        }

        private fun ResultSet.getCategories(columnName: String) : Array<Categories> {
            val cats = getArray(columnName).array as IntArray

            return cats.map {
                categoryRepo.byId(it) ?: TODO("FOREIGN KEY EXCEPTION")
            }.groupBy {
                it.first
            }.map { pair ->
                Categories(
                    pair.value.map { it.second },
                    language = pair.key
                )
            }.toTypedArray()
        }

    }
}

class UnknownCalendarComponentTypeException(message: String) : Exception(message)


private fun ResultSet.getDatetime(columnName: String) : DateTime {
    val offsetTime = getObject(columnName, OffsetDateTime::class.java)
    return DateTime(
        DateType(
            offsetTime.year,
            offsetTime.monthValue,
            offsetTime.dayOfMonth
        ),
        Time(
            offsetTime.hour,
            offsetTime.minute,
            offsetTime.second // TODO("Add offset")
        )
    )
}

private fun ResultSet.getAttachments(columnName: String) : Array<Attachment> {
    val att = getArray(columnName).array as Array<String>

    return att.map {
        Attachment(
            Uri(it)
        )
    }.toTypedArray()
}