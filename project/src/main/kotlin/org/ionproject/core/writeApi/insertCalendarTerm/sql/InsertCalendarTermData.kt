package org.ionproject.core.writeApi.insertCalendarTerm.sql

object InsertCalendarTerm {
    const val SCHEMA = "dbo"

    const val UPSERT_SCHOOL_PROC = "sp_createOrReplaceSchool"
    const val UPSERT_COURSE_PROC = "sp_createOrReplaceCourse"
    const val CREATE_CLASS_SECTION_EVENT_PROC = "sp_createClassSectionEvent"
    const val INSERT_CAL_TERM = "sp_insertCalTerm"

    const val ID = "id"
    const val SCHOOL_NAME_PARAM = "schoolName"
    const val SCHOOL_ACR_PARAM = "schoolAcr"
    const val COURSE_NAME_PARAM = "courseName"
    const val COURSE_ACR_PARAM = "courseAcr"
    const val PROGRAMME_NAME_PARAM = "programmeName"
    const val PROGRAMME_ACR_PARAM = "programmeAcr"
    const val PROGRAMME_TERM_SIZE_PARAM = "programmeTermSize"
    const val CALENDAR_TERM_PARAM = "calTerm"
    const val CALENDAR_SECTION_PARAM = "calendarSection"
    const val LANGUAGE_PARAM = "lang"
    const val CATEGORY_PARAM = "categid"
    const val SUMMARY_PARAM = "summary"
    const val DESCRIPTION_PARAM = "description"
    const val DTSTART_PARAM = "dtstart"
    const val DTEND_PARAM = "dtend"
    const val WEEK_DAYS_PARAM = "week_days"
    const val LOCATION_PARAM = "location"

    const val CALL_UPSERT_SCHOOL = """
    CALL $SCHEMA.$UPSERT_SCHOOL_PROC(
        :$SCHOOL_NAME_PARAM,
        :$SCHOOL_ACR_PARAM,
        :$PROGRAMME_NAME_PARAM,
        :$PROGRAMME_ACR_PARAM,
        :$PROGRAMME_TERM_SIZE_PARAM,
        :$CALENDAR_TERM_PARAM)
  """

    const val CALL_UPSERT_COURSE = """
    CALL $SCHEMA.$UPSERT_COURSE_PROC(
        :$COURSE_NAME_PARAM,
        :$COURSE_ACR_PARAM,
        :$CALENDAR_SECTION_PARAM,
        :$CALENDAR_TERM_PARAM,
        :$LANGUAGE_PARAM)
  """

    const val CALL_CREATE_EVENT = """
    CALL $SCHEMA.$CREATE_CLASS_SECTION_EVENT_PROC(
        :$COURSE_NAME_PARAM,
        :$COURSE_ACR_PARAM,
        :$CALENDAR_SECTION_PARAM,
        :$CALENDAR_TERM_PARAM,
        :$SUMMARY_PARAM,
        :$DESCRIPTION_PARAM,
        :$LANGUAGE_PARAM,
        :$CATEGORY_PARAM,
        :$DTSTART_PARAM,
        :$DTEND_PARAM,
        :$WEEK_DAYS_PARAM,
        :$LOCATION_PARAM)
  """

    const val INSERT_CAL_TERM_DATES = """
    CALL $SCHEMA.$INSERT_CAL_TERM(
        :$ID, 
        :$DTSTART_PARAM, 
        :$DTEND_PARAM)
    """
}
