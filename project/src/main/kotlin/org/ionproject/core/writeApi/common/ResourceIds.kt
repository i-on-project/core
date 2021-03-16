package org.ionproject.core.writeApi.common

/**
 * Resource ID's identify the resource trying to access, this info is used to
 * validate the access to the presented token, instead of comparing by URI.
 *
 * The resource identifiers here must match with the resource id in the policies table
 *
 * TODO(IMPORT THOSE FROM DATABASE INSTEAD OF HAVING TO ALTER BOTH SIDES)
 * TODO(OVERRIDE ERROR ENDPOINT AS THIS CURRENT METHOD WONT ALLOW ACCESS TO IT)
 */
object ResourceIds {
    const val VERSION = "v0"
    const val ALL_VERSIONS = "*"

    // ERROR
    const val ERROR = "error"

    const val INSERT_CLASS_SECTION_EVENTS = "insertClassSectionEvents"
    const val INSERT_CLASS_SECTION_FACULTY = "insertClassSectionFaculty"
    const val INSERT_CALENDAR_TERM = "insertCalendarTerm"
}
