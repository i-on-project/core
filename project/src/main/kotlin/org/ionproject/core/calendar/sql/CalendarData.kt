package org.ionproject.core.calendar.sql

import org.ionproject.core.bind
import org.ionproject.core.common.querybuilder.*
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.statement.Query
import org.springframework.util.MultiValueMap

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
    const val UNTIL = "until"
    const val DUE = "due"
    const val LOCATION = "location"

    // Tables
    const val CLASS = "dbo.Class"
    const val COURSE = "dbo.Course"
    const val CLASS_SECTION = "dbo.ClassSection"

    // Column names
    const val CALENDAR = "calendar"
    const val COURSE_ID = "courseId"
    const val CAL_TERM = "calendarterm"
    const val CLASS_ID = "classid"
    const val CAS_ID = "cas_id"
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
        $BYDAY,
        $UNTIL,
        $LOCATION
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
        $BYDAY,
        $UNTIL,
        $LOCATION
    """

    const val CALENDAR_COMPONENT_FROM_CLASS_QUERY =
        """with calendar_id as (
                    select $CLASS.$CALENDAR
                    from $CLASS
                    where $CLASS.$COURSE_ID = :$COURSE_ID and $CLASS.$CAL_TERM = :$CAL_TERM
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
                    join
                        $CLASS on $CLASS.$ID=$CLASS_SECTION.$CLASS_ID
                    where 
                        $CLASS.$COURSE_ID = :$COURSE_ID 
                        and
                        $CLASS.$CAL_TERM = :$CAL_TERM
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
            .with(
                """with calendar_id as (
                    select $CLASS.$CALENDAR
                    from $CLASS
                    where $CLASS.$COURSE_ID = :$COURSE_ID and $CLASS.$CAL_TERM = :$CAL_TERM
                )"""
            )
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
                BYDAY,
                UNTIL,
                LOCATION
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
                BYDAY,
                UNTIL,
                LOCATION
            ).build()

        val query = handle.createQuery(queryString)
            .bind(COURSE_ID, courseId)
            .bind(CAL_TERM, calendarTerm)
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
            .with(
                """with calendar_id as (
                    select 
                        $CLASS_SECTION.$CALENDAR
                    from 
                        $CLASS_SECTION
                    join
                        $CLASS on $CLASS.$ID=$CLASS_SECTION.$CLASS_ID
                    where 
                        $CLASS.$COURSE_ID = :$COURSE_ID 
                        and
                        $CLASS.$CAL_TERM = :$CAL_TERM
                        and
                        $CLASS_SECTION.$ID = :$ID
                )"""
            )
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
                BYDAY,
                UNTIL,
                LOCATION
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
                BYDAY,
                UNTIL,
                LOCATION
            ).build()

        val query = handle.createQuery(queryString)
            .bind(COURSE_ID, courseId)
            .bind(CAL_TERM, calendarTerm)
            .bind(ID, classSectionId)
            .bind(QUERY_FILTERS, filters)

        return query
    }

    const val CHECK_IF_CLASS_EXISTS = """
        select count(*) from $COURSE as CO
        join $CLASS as CA on CO.$ID=CA.$COURSE_ID
        where $CAL_TERM=:$CAL_TERM AND CO.$ID=:$ID
        """

    const val CHECK_IF_CLASS_SECTION_EXISTS = """
        select count(*) from $COURSE as CO
        join $CLASS as C on CO.$ID=C.$COURSE_ID
        join $CLASS_SECTION as CAS on C.$ID=CAS.$CLASS_ID
        where C.$CAL_TERM=:$CAL_TERM AND CO.$ID=:$ID AND CAS.$ID=:$CAS_ID
        """
}
