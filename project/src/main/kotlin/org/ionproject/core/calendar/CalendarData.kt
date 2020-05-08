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
    // Name of Calendar component view and of calendar property columns
    private const val CALENDAR_COMPONENT = "dbo.v_ComponentsAll"
    private const val UID = "uid"
    private const val CALENDARS = "calendars"
    private const val TYPE = "type"
    private const val SUMMARIES = "summaries"
    private const val SUMMARIES_LANGUAGE = "summaries_language"
    private const val DESCRIPTIONS = "descriptions"
    private const val DESCRIPTIONS_LANGUAGE = "descriptions_language"
    private const val CATEGORIES = "categories"
    private const val DTSTAMP = "dtstamp"
    private const val CREATED = "created"
    private const val ATTACHMENTS = "attachments"
    private const val DTSTART = "dtstart"
    private const val DTEND = "dtend"
    private const val BYDAY = "byday"
    private const val DUE = "due"

    // Class and ClassSection table and column names
    private const val CLASS = "dbo.Class"
    private const val CLASS_SECTION = "dbo.ClassSection"
    private const val CALENDAR = "calendar"
    private const val COURSE = "courseId"
    private const val TERM = "term"
    private const val ID = "id"

    // Desired columns from the $CALENDAR_COMPONENT table/view when querying to get desired mapping functionality
    private const val SELECT = """
    select 
        $UID,
        $TYPE,
        array_agg(distinct merge_language_text($SUMMARIES_LANGUAGE, $SUMMARIES)) as $SUMMARIES,
        array_agg(distinct merge_language_text($DESCRIPTIONS_LANGUAGE, $DESCRIPTIONS)) as $DESCRIPTIONS,
        array_agg(distinct $CATEGORIES) as $CATEGORIES,
        array_agg(distinct $ATTACHMENTS) as $ATTACHMENTS,
        $DTSTAMP,
        $CREATED,
        $DTSTART,
        $DTEND,
        $DUE,
        $BYDAY
    """

    // This group by is necessary when queries use $SELECT, otherwise the use of array_agg will launch an SQL exception
    private const val GROUP_BY = """group by
        $UID, 
        $TYPE,
        $DTSTAMP,
        $CREATED,
        $DTSTART,
        $DTEND,
        $DUE,
        $BYDAY"""

    const val CALENDAR_FROM_CLASS_QUERY =
        """with calendar_id as (
                    select $CLASS.$CALENDAR
                    from $CLASS
                    where $CLASS.$COURSE = :$COURSE and $CLASS.$TERM = :$TERM
                )
            $SELECT
            from
                $CALENDAR_COMPONENT
            where 
                $CALENDARS = (select * from calendar_id) 
            $GROUP_BY"""

    const val CALENDAR_COMPONENT_FROM_CLASS_QUERY =
        """with calendar_id as (
                    select $CLASS.$CALENDAR
                    from $CLASS
                    where $CLASS.$COURSE = :$COURSE and $CLASS.$TERM = :$TERM
                )
            $SELECT
            from 
                $CALENDAR_COMPONENT
            where 
                $CALENDARS = (select * from calendar_id)
                and
                $UID = :$UID
            $GROUP_BY"""

    const val CALENDAR_FROM_CLASS_SECTION_QUERY =
        """with calendar_id as (
                    select 
                        $CLASS_SECTION.$CALENDAR
                    from 
                        $CLASS_SECTION
                    where 
                        $CLASS_SECTION.$COURSE = :$COURSE 
                        and
                        $CLASS_SECTION.$TERM = :$TERM
                        and
                        $CLASS_SECTION.$ID = :$ID
                )
            $SELECT
            from $CALENDAR_COMPONENT
            where $CALENDARS = (select * from calendar_id)
            $GROUP_BY"""

    const val CALENDAR_COMPONENT_FROM_CLASS_SECTION_QUERY =
        """with calendar_id as (
                    select 
                        $CLASS_SECTION.$CALENDAR
                    from 
                        $CLASS_SECTION
                    where 
                        $CLASS_SECTION.$COURSE = :$COURSE 
                        and
                        $CLASS_SECTION.$TERM = :$TERM
                        and
                        $CLASS_SECTION.$ID = :$ID
                )
            $SELECT
            from $CALENDAR_COMPONENT
            where
                $CALENDARS = (select * from calendar_id)
                and
                $UID = :$UID
            $GROUP_BY"""

    @Component
    class CalendarComponentMapper(
        private val languageRepo: LanguageRepo,
        private val categoryRepo: CategoryRepo
    ) : RowMapper<CalendarComponent> {

        override fun map(rs: ResultSet, ctx: StatementContext): CalendarComponent {
            val type = rs.getString(TYPE)
            val uid = UniqueIdentifier(rs.getInt(UID).toString(16)) // TODO(use constant for uid radix)
            val categories = rs.getCategories(CATEGORIES)
            val summary = rs.getSummaries(SUMMARIES)
            val description = rs.getDescriptions(DESCRIPTIONS)
            val dtStamp = DateTimeStamp(rs.getDatetime(DTSTAMP))
            val created = DateTimeCreated(rs.getDatetime(CREATED))

            return when (type) {
                "E" -> Event(
                    uid,
                    summary,
                    description,
                    dtStamp,
                    created,
                    categories,
                    DateTimeStart(rs.getDatetime(DTSTART)),
                    DateTimeEnd(rs.getDatetime(DTEND)),
                    rs.getRecurrenceRule(BYDAY)
                )
                "J" -> Journal(
                    uid,
                    summary,
                    description,
                    rs.getAttachments(ATTACHMENTS),
                    dtStamp,
                    DateTimeStart(rs.getDatetime(DTSTART)),
                    created,
                    categories
                )
                "T" -> Todo(
                    uid,
                    summary,
                    description,
                    rs.getAttachments(ATTACHMENTS),
                    dtStamp,
                    created,
                    DateTimeDue(rs.getDatetime(DUE)),
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