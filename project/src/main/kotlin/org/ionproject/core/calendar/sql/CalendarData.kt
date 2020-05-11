package org.ionproject.core.calendar.sql

import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.Time
import org.ionproject.core.calendar.icalendar.types.Uri
import java.sql.ResultSet
import java.time.OffsetDateTime
import org.ionproject.core.calendar.icalendar.types.Date as DateType

object CalendarData {
    // Name of Calendar component view and of calendar property columns
    const val CALENDAR_COMPONENT = "dbo.v_ComponentsAll"
    const val UID = "uid"
    const val CALENDARS = "calendars"
    const val TYPE = "type"
    const val SUMMARIES = "summaries"
    const val SUMMARIES_LANGUAGE = "summaries_language"
    const val DESCRIPTIONS = "descriptions"
    const val DESCRIPTIONS_LANGUAGE = "descriptions_language"
    const val CATEGORIES = "categories"
    const val DTSTAMP = "dtstamp"
    const val CREATED = "created"
    const val ATTACHMENTS = "attachments"
    const val DTSTART = "dtstart"
    const val DTEND = "dtend"
    const val BYDAY = "byday"
    const val DUE = "due"

    // Class and ClassSection table and column names
    const val CLASS = "dbo.Class"
    const val CLASS_SECTION = "dbo.ClassSection"
    const val CALENDAR = "calendar"
    const val COURSE = "courseId"
    const val TERM = "term"
    const val ID = "id"

    // Desired columns from the $CALENDAR_COMPONENT table/view when querying to get desired mapping functionality
    const val SELECT = """
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
    const val GROUP_BY = """group by
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
}
