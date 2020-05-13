package org.ionproject.core.calendar.sql

import org.ionproject.core.bind
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.Time
import org.ionproject.core.calendar.icalendar.types.Uri
import org.ionproject.core.common.querybuilder.*
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.statement.Query
import org.springframework.util.MultiValueMap
import java.sql.ResultSet
import java.time.OffsetDateTime
import org.ionproject.core.calendar.icalendar.types.Date as DateType

object CalendarData {
        // Name of Calendar component view and of calendar property columns
    private const val CALENDAR_COMPONENT = "dbo.v_ComponentsAll"
    const val UID = "uid"
    private const val CALENDARS = "calendars"
    const val TYPE = "type"
    const val SUMMARIES = "summaries"
    private const val SUMMARIES_LANGUAGE = "summaries_language"
    const val DESCRIPTIONS = "descriptions"
    private const val DESCRIPTIONS_LANGUAGE = "descriptions_language"
    const val CATEGORIES = "categories"
    const val DTSTAMP = "dtstamp"
    const val CREATED = "created"
    const val ATTACHMENTS = "attachments"
    const val DTSTART = "dtstart"
    const val DTEND = "dtend"
    const val BYDAY = "byday"
    const val DUE = "due"

    // Class and ClassSection table and column names
    private const val CLASS = "dbo.Class"
    private const val CLASS_SECTION = "dbo.ClassSection"
    private const val CALENDAR = "calendar"
    const val COURSE = "courseId"
    const val TERM = "term"
    const val ID = "id"

    // Desired columns from the $CALENDAR_COMPONENT table/view when querying to get desired mapping functionality
    private const val SELECT = """
    select 
        $UID,
        $TYPE,
        array_agg(distinct ($SUMMARIES_LANGUAGE, $SUMMARIES)) as $SUMMARIES,
        array_agg(distinct ($DESCRIPTIONS_LANGUAGE, $DESCRIPTIONS)) as $DESCRIPTIONS,
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

    // Query parameters
    private const val START_BEFORE = "startBefore"
    private const val START_AFTER = "startAfter"
    private const val END_BEFORE = "endBefore"
    private const val END_AFTER = "endAfter"
    private const val SUMMARY = "summary"

    private val QUERY_FILTERS = mapOf(
        TYPE to VarcharCondition(TYPE, ComparisonOperator.EQUALS, TYPE, AggregationOperator.OR),
        START_BEFORE to TimestampCondition(DTSTART, ComparisonOperator.LESSER_THAN, START_BEFORE),
        START_AFTER to TimestampCondition(DTSTART, ComparisonOperator.GREATER_THAN, START_AFTER),
        END_BEFORE to TimestampCondition(DTEND, ComparisonOperator.LESSER_THAN, END_BEFORE),
        END_AFTER to TimestampCondition(DTEND, ComparisonOperator.GREATER_THAN, END_AFTER),
        SUMMARY to VarcharCondition(SUMMARIES, ComparisonOperator.LIKE, SUMMARY)
    )

    fun calendarFromClassQuery(
        handle: Handle,
        courseId: Int,
        calendarTerm: String,
        filters: MultiValueMap<String, String>
    ): Query {
        val queryString = SQLQueryBuilder()
            .with("""with calendar_id as (
                    select $CLASS.$CALENDAR
                    from $CLASS
                    where $CLASS.$COURSE = :$COURSE and $CLASS.$TERM = :$TERM
                )""")
            .select(
                UID,
                TYPE,
                "array_agg(distinct ($SUMMARIES_LANGUAGE, $SUMMARIES)) as $SUMMARIES",
                "array_agg(distinct ($DESCRIPTIONS_LANGUAGE, $DESCRIPTIONS)) as $DESCRIPTIONS",
                "array_agg(distinct $CATEGORIES) as $CATEGORIES",
                "array_agg(distinct $ATTACHMENTS) as $ATTACHMENTS",
                DTSTAMP,
                CREATED,
                DTSTART,
                DTEND,
                DUE,
                BYDAY
            ).from(
                CALENDAR_COMPONENT
            ).where(
                "$CALENDARS = (select * from calendar_id)"
            ).where(
                QUERY_FILTERS,
                filters
            ).groupBy(
                UID,
                TYPE,
                DTSTAMP,
                CREATED,
                DTSTART,
                DTEND,
                DUE,
                BYDAY
            ).build()

        val query = handle.createQuery(queryString)
            .bind(COURSE, courseId)
            .bind(TERM, calendarTerm)
            .bind(QUERY_FILTERS, filters)

        return query
    }

    fun calendarFromClassSectionQuery(
        handle: Handle,
        courseId: Int,
        calendarTerm: String,
        classSectionId: String,
        filters: MultiValueMap<String, String>
    ): Query {
        val queryString = SQLQueryBuilder()
            .with("""with calendar_id as (
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
                )""")
            .select(
                UID,
                TYPE,
                "array_agg(distinct ($SUMMARIES_LANGUAGE, $SUMMARIES)) as $SUMMARIES",
                "array_agg(distinct ($DESCRIPTIONS_LANGUAGE, $DESCRIPTIONS)) as $DESCRIPTIONS",
                "array_agg(distinct $CATEGORIES) as $CATEGORIES",
                "array_agg(distinct $ATTACHMENTS) as $ATTACHMENTS",
                DTSTAMP,
                CREATED,
                DTSTART,
                DTEND,
                DUE,
                BYDAY
            ).from(
                CALENDAR_COMPONENT
            ).where(
                "$CALENDARS = (select * from calendar_id)"
            ).where(
                QUERY_FILTERS,
                filters
            ).groupBy(
                UID,
                TYPE,
                DTSTAMP,
                CREATED,
                DTSTART,
                DTEND,
                DUE,
                BYDAY
            ).build()

        val query = handle.createQuery(queryString)
            .bind(COURSE, courseId)
            .bind(TERM, calendarTerm)
            .bind(ID, classSectionId)
            .bind(QUERY_FILTERS, filters)

        return query
    }
}
