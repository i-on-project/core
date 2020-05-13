package org.ionproject.core.klass.sql

internal object KlassData {
    const val SCHEMA = "dbo"
    const val CLASS = "class"

    const val ID = "id"
    const val CID = "courseid"
    const val SID = "sid"
    const val ACR = "acronym"
    const val CAL_TERM = "calendarterm"
    const val OFFSET = "page"
    const val LIMIT = "limit"

    const val COURSE = "course"
    const val COURSE_ID = "id"
    const val COURSE_ACR = "acronym"

    const val CLASS_SECTION = "classSection"
    const val CLASS_SECTION_ID = "id"
    const val CLASS_SECTION_CLASS_ID = "classid"

    const val GET_CLASS_QUERY = """
        select CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM
        from $SCHEMA.$CLASS as C
        join $SCHEMA.$COURSE as CR on C.$CID=CR.$COURSE_ID
        where CR.$COURSE_ID=:$CID and C.$CAL_TERM=:$CAL_TERM
    """

    const val GET_CLASS_SECTIONS_QUERY = """
        select CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM, CS.$CLASS_SECTION_ID as $SID
        from $SCHEMA.$CLASS as C
        join $SCHEMA.$CLASS_SECTION as CS on C.$ID=CS.$CLASS_SECTION_CLASS_ID
        join $SCHEMA.$COURSE as CR on CR.$COURSE_ID=C.$CID
        where C.$CID=:$CID and C.$CAL_TERM=:$CAL_TERM
        order by $SID
    """

    const val GET_CLASSES_QUERY = """
        select CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM
        from $SCHEMA.$CLASS as C
        join $SCHEMA.$COURSE as CR on C.$CID=CR.$COURSE_ID
        where C.$CID=:$CID order by C.$CAL_TERM offset :$OFFSET limit :$LIMIT
    """
}
