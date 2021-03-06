package org.ionproject.core.calendarTerm.sql

import org.ionproject.core.common.Uri
import org.ionproject.core.search.SearchableEntities
import org.ionproject.core.search.sql.SearchData

internal object CalendarTermData {
    const val SCHEMA = "dbo"
    const val CALENDAR_TERM = "calendarTerm"
    const val DOCUMENT = "document"
    const val CLASS = "class"
    const val OFFSET = "offset"
    const val LIMIT = "lim"
    const val ID = "id"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val EXAM_SEASON = "ExamSeason"

    const val CLASS_CAL_TERM = "calendarterm"

    const val SEARCH_CALENDAR_TERMS = """
        select
            '${SearchableEntities.CALENDAR_TERM}' as ${SearchData.TYPE},
            $ID::VARCHAR(32) as ${SearchData.ID},
            $ID as ${SearchData.NAME},
            '${Uri.calendarTerms}/' || $ID as ${SearchData.HREF},
            ts_rank($DOCUMENT, ${SearchData.QUERY}) as ${SearchData.RANK}
        from $SCHEMA.$CALENDAR_TERM
        where $DOCUMENT @@ ${SearchData.QUERY}
    """

    const val CALENDAR_TERMS_QUERY = """
        select $ID, $START_DATE, $END_DATE
        from $SCHEMA.$CALENDAR_TERM
        order by $START_DATE desc
        offset :$OFFSET limit :$LIMIT
    """

    const val CALENDAR_TERM_QUERY = """
        select $ID, $START_DATE, $END_DATE
        from $SCHEMA.$CALENDAR_TERM
        where $ID=:$ID
    """

    const val CLASSES_QUERY = """
        select *
        from $SCHEMA.$CLASS
        where $CLASS_CAL_TERM=:$ID offset :$OFFSET limit :$LIMIT
    """

    const val EXAM_SEASONS_QUERY = """
        select *
        from $SCHEMA.$EXAM_SEASON
        where $CALENDAR_TERM=:$CALENDAR_TERM
    """
}
