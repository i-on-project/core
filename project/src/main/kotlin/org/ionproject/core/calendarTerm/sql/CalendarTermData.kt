package org.ionproject.core.calendarTerm.sql

internal object CalendarTermData {
    const val SCHEMA = "dbo"
    const val CALENDAR_TERM = "calendarTerm"
    const val CLASS = "class"
    const val OFFSET = "offset"
    const val LIMIT = "lim"
    const val ID = "id"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"

    const val CLASS_CAL_TERM = "term"

    const val CALENDAR_TERMS_QUERY = """
        select $ID, $START_DATE, $END_DATE
        from $SCHEMA.$CALENDAR_TERM
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
}