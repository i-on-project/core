package org.ionproject.core.course.sql

internal object CourseData {
  const val SCHEMA = "dbo"
  const val COURSE = "CourseWithTerm"
  const val ID = "id"
  const val ACR = "acronym"
  const val NAME = "name"
  const val CAL_TERM = "calendarterm"
  const val LIMIT = "lim"
  const val OFFSET = "offset"

  const val GET_COURSES_QUERY = """select $ID, $ACR, $NAME, $CAL_TERM
        from $COURSE
        order by $ID
        offset :$OFFSET limit :$LIMIT"""

  const val GET_COURSE_QUERY = """select $ID, $ACR, $NAME, $CAL_TERM
        from $COURSE
        where $ID=:$ID"""
}
