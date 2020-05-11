package org.ionproject.core.classSection.sql

internal object ClassSectionData {
    const val SCHEMA = "dbo"
    const val CLASS_SECTION = "classSection"

    const val ID = "id"
    const val CID = "courseid"
    const val ACR = "acronym"
    const val CAL_TERM = "term"

    const val COURSE = "course"
    const val COURSE_ID = "id"
    const val COURSE_ACR = "acronym"

    const val CLASS_SECTION_QUERY = """
       select CR.$COURSE_ID as $CID, CR.$COURSE_ACR, CS.$CAL_TERM, CS.$ID
       from $SCHEMA.$CLASS_SECTION as CS
       join $SCHEMA.$COURSE as CR on CS.$CID=CR.$COURSE_ID
       where CR.$COURSE_ID=:$CID and term=:$CAL_TERM and CS.$ID=:$ID 
    """
}

