package org.ionproject.core.classSection.sql

import org.ionproject.core.common.Uri
import org.ionproject.core.search.SearchableEntities
import org.ionproject.core.search.sql.SearchData

internal object ClassSectionData {
    const val SCHEMA = "dbo"
    const val CLASS_SECTION = "classSection"

    const val ID = "id"
    const val PARENT_CLASS_ID = "classid"
    const val DOCUMENT = "document"

    const val COURSE = "course"
    const val COURSE_ID = "id"
    const val COURSE_ACR = "acronym"

    const val CLASS = "class"
    const val CLASS_ID = "id"
    const val CID = "courseid"
    const val ACR = "acronym"
    const val CAL_TERM = "calendarterm"

    const val SEARCH_CLASS_SECTIONS = """
        select
            '${SearchableEntities.CLASS_SECTION}' as ${SearchData.TYPE}
            $CLASS_SECTION.$ID as ${SearchData.ID},
            $COURSE.$ACR || ' ' || $CLASS.$CAL_TERM || ' ' || $CLASS_SECTION.$ID as ${SearchData.NAME},
            '${Uri.courses}/' || $COURSE.$ID || '/classes/' || $CLASS.$CAL_TERM || '/' || $CLASS_SECTION.$ID as ${SearchData.HREF}
        from $CLASS_SECTION
        join $CLASS on $CLASS.$ID = $CLASS_SECTION.$PARENT_CLASS_ID
        join $COURSE ON $COURSE.$ID = $CLASS.$CID
        where $DOCUMENT @@ :query
    """

    const val CLASS_SECTION_QUERY = """
       select CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM, CS.$ID
       from $SCHEMA.$CLASS_SECTION as CS
       join $SCHEMA.$CLASS as C on C.$CLASS_ID=CS.$PARENT_CLASS_ID
       join $SCHEMA.$COURSE as CR on C.$CID=CR.$COURSE_ID
       where CR.$COURSE_ID=:$CID and $CAL_TERM=:$CAL_TERM and CS.$ID=:$ID 
    """
}

