package org.ionproject.core.classSection.sql

internal object ClassSectionData {
  const val SCHEMA = "dbo"
  const val CLASS_SECTION = "classSection"

  const val ID = "id"
  const val PARENT_CLASS_ID = "classid"

  const val COURSE = "course"
  const val COURSE_ID = "id"
  const val COURSE_ACR = "acronym"

  const val CLASS = "class"
  const val CLASS_ID = "id"
  const val CID = "courseid"
  const val ACR = "acronym"
  const val CAL_TERM = "calendarterm"

  const val CLASS_SECTION_QUERY = """
       select CR.$COURSE_ID as $CID, CR.$COURSE_ACR, C.$CAL_TERM, CS.$ID
       from $SCHEMA.$CLASS_SECTION as CS
       join $SCHEMA.$CLASS as C on C.$CLASS_ID=CS.$PARENT_CLASS_ID
       join $SCHEMA.$COURSE as CR on C.$CID=CR.$COURSE_ID
       where CR.$COURSE_ID=:$CID and $CAL_TERM=:$CAL_TERM and CS.$ID=:$ID 
    """
}

