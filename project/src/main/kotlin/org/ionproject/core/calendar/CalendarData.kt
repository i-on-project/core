package org.ionproject.core.calendar

import org.ionproject.core.calendar.category.CategoryRepo
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.Event
import org.ionproject.core.calendar.icalendar.Journal
import org.ionproject.core.calendar.icalendar.Todo
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
import java.sql.ResultSet
import java.time.OffsetDateTime
import org.ionproject.core.calendar.icalendar.types.Date as DateType

object CalendarData {
    private const val CALENDAR_COMPONENT_ABBREVIATION = "cmp"
    private const val CALENDAR_COMPONENT = "dbo.v_ComponentsAll $CALENDAR_COMPONENT_ABBREVIATION"

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
    select 
        $UID,
        $TYPE,
        array_agg(distinct merge_language_text($SUMMARIES_LANGUAGE, $SUMMARIES)) as $SUMMARIES_COLUMN,
        array_agg(distinct merge_language_text($DESCRIPTIONS_LANGUAGE, $DESCRIPTIONS)) as $DESCRIPTIONS_COLUMN,
        array_agg(distinct $CATEGORIES) as $CATEGORIES_COLUMN,
        array_agg(distinct $ATTACHMENTS) as $ATTACHMENTS_COLUMN,
        $DTSTAMP,
        $CREATED,
        $DTSTART,
        $DTEND,
        $DUE,
        $BYDAY
    """

    private const val GROUP_BY = """group by
        $UID, 
        $TYPE,
        $DTSTAMP,
        $CREATED,
        $DTSTART,
        $DTEND,
        $DUE,
        $BYDAY"""

    private const val CLASS_ABBREVIATION = "c"
    private const val CLASS = "dbo.Class $CLASS_ABBREVIATION"

    private const val CALENDAR_ID_COLUMN = "calendar"
    private const val COURSE_COLUMN = "courseId"
    private const val TERM_COLUMN = "term"
    private const val ID_COLUMN = "id"

    private const val CLASS_CALENDAR_ID = "$CLASS_ABBREVIATION.$CALENDAR_ID_COLUMN"
    private const val CLASS_COURSE = "$CLASS_ABBREVIATION.$COURSE_COLUMN"
    private const val CLASS_TERM = "$CLASS_ABBREVIATION.$TERM_COLUMN"

    private const val CLASS_SECTION_ABBREVIATION = "cs"
    private const val CLASS_SECTION = "dbo.Class $CLASS_SECTION_ABBREVIATION"

    private const val CLASS_SECTION_CALENDAR_ID = "$CLASS_SECTION_ABBREVIATION.$CALENDAR_ID_COLUMN"
    private const val CLASS_SECTION_ID = "$CLASS_SECTION_ABBREVIATION.$ID_COLUMN"
    private const val CLASS_SECTION_COURSE = "$CLASS_SECTION_ABBREVIATION.$COURSE_COLUMN"
    private const val CLASS_SECTION_TERM = "$CLASS_SECTION_ABBREVIATION.$TERM_COLUMN"

    const val CALENDAR_FROM_CLASS_QUERY = """with calendar_id as (
                    select $CLASS_CALENDAR_ID
                    from $CLASS
                    where $CLASS_COURSE = :$COURSE_COLUMN and $CLASS_TERM = :$TERM_COLUMN
                )
            $SELECT
            from
                $CALENDAR_COMPONENT
            where $CALENDARS = (select * from calendar_id)
            $GROUP_BY"""

    const val CALENDAR_COMPONENT_FROM_CLASS_QUERY = """with calendar_id as (
                    select $CLASS_CALENDAR_ID
                    from $CLASS
                    where $CLASS_COURSE = :$COURSE_COLUMN and $CLASS_TERM = :$TERM_COLUMN
                )
            $SELECT
            from $CALENDAR_COMPONENT
            where $CALENDARS = (select * from calendar_id) and $UID = :$UID_COLUMN
            $GROUP_BY"""

    const val CALENDAR_FROM_CLASS_SECTION_QUERY =
        """with calendar_id as (
                    select $CLASS_SECTION_CALENDAR_ID
                    from $CLASS_SECTION
                    where 
                        $CLASS_SECTION_COURSE = :$COURSE_COLUMN 
                        and
                        $CLASS_SECTION_TERM = :$TERM_COLUMN
                        and
                        $CLASS_SECTION_ID = :$ID_COLUMN
                )
            $SELECT
            from $CALENDAR_COMPONENT
            where $CALENDARS = (select * from calendar_id)
            $GROUP_BY"""

    const val CALENDAR_COMPONENT_FROM_CLASS_SECTION_QUERY = """with calendar_id as (
                    select $CLASS_SECTION_CALENDAR_ID
                    from $CLASS_SECTION
                    where 
                        $CLASS_SECTION_COURSE = :$COURSE_COLUMN 
                        and
                        $CLASS_SECTION_TERM = :$TERM_COLUMN
                        and
                        $CLASS_SECTION_ID = :$ID_COLUMN
                )
            $SELECT
            from $CALENDAR_COMPONENT
            where $CALENDARS = (select * from calendar_id) and $UID = :$UID_COLUMN
            $GROUP_BY"""

    @Component
    class CalendarComponentMapper(
        private val languageRepo: LanguageRepo,
        private val categoryRepo: CategoryRepo
    ) : RowMapper<CalendarComponent> {

        override fun map(rs: ResultSet, ctx: StatementContext): CalendarComponent {
            val type = rs.getString(TYPE_COLUMN)
            val uid = UniqueIdentifier(rs.getInt(UID_COLUMN).toString(16)) // TODO(use constant for uid radix)
            val categories = rs.getCategories(CATEGORIES_COLUMN)
            val summary = rs.getSummaries(SUMMARIES_COLUMN)
            val description = rs.getDescriptions(DESCRIPTIONS_COLUMN)
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
                    DateTimeStart(rs.getDatetime(DTSTART_COLUMN)),
                    DateTimeEnd(rs.getDatetime(DTEND_COLUMN)),
                    rs.getRecurrenceRule(BYDAY_COLUMN)
                )
                "J" -> Journal(
                    uid,
                    summary,
                    description,
                    rs.getAttachments(ATTACHMENTS_COLUMN),
                    dtStamp,
                    DateTimeStart(rs.getDatetime(DTSTART_COLUMN)),
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
                    DateTimeDue(rs.getDatetime(DUE_COLUMN)),
                    categories
                )
                else -> throw UnknownCalendarComponentTypeException("The type represented by $type is not known.")
            }
        }

        private fun ResultSet.getSummaries(columnName: String): Array<Summary> {
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

        private fun ResultSet.getDescriptions(columnName: String): Array<Description> {
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

        private fun ResultSet.getCategories(columnName: String): Array<Categories> {
            val tempCats = getArray(columnName).array as Array<Int>

            val cats = tempCats.map { it }

            return cats.map {
                categoryRepo.byId(it) ?: TODO("FOREIGN KEY EXCEPTION")
            }.groupBy {
                it.language // group categories of this event by language
            }.map { pair ->
                val categories = pair.value

                Categories(
                    categories.map { it.value },
                    language = pair.key
                )
            }.toTypedArray()
        }

        private fun ResultSet.getRecurrenceRule(columnName: String): RecurrenceRule? {
            val weekDays = getString(columnName)?.split(",")?.map { WeekDay(WeekDay.Weekday.valueOf(it), null) }

            return if (weekDays == null) null
            else RecurrenceRule(
                Recur(byDay = weekDays)
            )
        }

    }
}

class UnknownCalendarComponentTypeException(message: String) : Exception(message)


private fun ResultSet.getDatetime(columnName: String): DateTime {
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

private fun ResultSet.getAttachments(columnName: String): Array<Attachment> {
    val att = getArray(columnName).array as Array<String?>

    val attachments = mutableListOf<Attachment>()

    att.forEach {
        if (it == null) return emptyArray()

        attachments.add(
            Attachment(Uri(it))
        )
    }

    return attachments.toTypedArray()
}