package org.ionproject.core.klass.sql

import org.ionproject.core.common.Uri
import org.ionproject.core.search.SearchableEntities
import org.ionproject.core.search.sql.SearchData

internal object KlassData {
    const val SCHEMA = "dbo"
    const val CLASS = "class"

    const val ID = "id"
    const val DOCUMENT = "document"
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

    const val SEARCH_CLASSES = """
        select
            '${SearchableEntities.CLASS}' as ${SearchData.TYPE},
            CL.$ID::VARCHAR(32) as ${SearchData.ID},
            CR.$COURSE_ACR || ' ' || $CAL_TERM as ${SearchData.NAME},
            '${Uri.courses}/' || CR.$ID || '/classes/' || CL.$CAL_TERM as ${SearchData.HREF},
            ts_rank(CR.$DOCUMENT || CL.$DOCUMENT, ${SearchData.QUERY}) as ${SearchData.RANK}
        from $SCHEMA.$CLASS CL
        join $SCHEMA.$COURSE CR on CL.$CID=CR.$COURSE_ID
        where CR.$DOCUMENT || CL.$DOCUMENT @@ ${SearchData.QUERY}
    """

    const val GET_CLASS_QUERY = """
        select C.$ID, CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM
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
        select C.$ID, CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM
        from $SCHEMA.$CLASS as C
        join $SCHEMA.$COURSE as CR on C.$CID=CR.$COURSE_ID
        where C.$CID=:$CID order by C.$CAL_TERM offset :$OFFSET limit :$LIMIT
    """

    const val CHECK_IF_COURSE_EXISTS = """
        select count(*) from $SCHEMA.$COURSE as C where C.$COURSE_ID=:$CID
    """
}
