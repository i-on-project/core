package org.ionproject.core.course.sql

import org.ionproject.core.common.Uri
import org.ionproject.core.search.SearchableEntities
import org.ionproject.core.search.sql.SearchData

internal object CourseData {
    const val SCHEMA = "dbo"
    const val COURSE = "course"
    const val COURSE_WITH_TERM = "$SCHEMA.CourseWithTerm"
    const val ID = "id"
    const val ACR = "acronym"
    const val DOCUMENT = "document"
    const val NAME = "name"
    const val CAL_TERM = "calendarterm"
    const val LIMIT = "lim"
    const val OFFSET = "offset"

    const val SEARCH_COURSES = """
        select
            '${SearchableEntities.COURSE}' as ${SearchData.TYPE},
            $ID::VARCHAR(32) as ${SearchData.ID},
            COALESCE($NAME,$ACR) as ${SearchData.NAME},
            '${Uri.courses}/' || $ID as ${SearchData.HREF},
            ts_rank($DOCUMENT, ${SearchData.QUERY}) as ${SearchData.RANK}
        from $SCHEMA.$COURSE
        where $DOCUMENT @@ ${SearchData.QUERY}
            
    """

    const val GET_COURSES_QUERY = """select $ID, $ACR, $NAME, $CAL_TERM
        from $COURSE_WITH_TERM
        order by $ID
        offset :$OFFSET limit :$LIMIT"""

    const val GET_COURSE_QUERY = """select $ID, $ACR, $NAME, $CAL_TERM
        from $COURSE_WITH_TERM
        where $ID=:$ID"""
}
