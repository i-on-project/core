package org.ionproject.core.common

/**
 * Resource ID's identify the resource trying to access, this info is used to
 * validate the access to the presented token, instead of comparing by URI.
 *
 * The resource identifiers here must match with the resource id in the policies table
 *
 */
object ResourceIds {
    const val VERSION_0 = "v0"
    const val ALL_VERSIONS = "*"

    // ERROR
    const val ERROR = "error"

    // HOME
    const val GET_HOME = "getHome"

    // SEARCH
    const val SEARCH = "search"

    // ACCESS MANAGER
    const val ISSUE_TOKEN = "issueToken"
    const val REVOKE_TOKEN = "revokeToken"
    const val IMPORT_CLASS_CALENDAR = "importClassCalendar"
    const val IMPORT_CLASS_SECTION_CALENDAR = "importClassSectionCalendar"

    // PROGRAMMES
    const val GET_PROGRAMMES = "getProgrammes"
    const val GET_PROGRAMME = "getProgramme"
    const val GET_OFFER = "getOffer"
    const val GET_OFFERS = "getOffers"

    // COURSES
    const val GET_COURSES = "getCourses"
    const val GET_COURSE = "getCourse"

    // CLASS
    const val GET_CLASSES = "getClasses"
    const val GET_CLASS = "getClass"

    // CLASS SECTION
    const val GET_CLASS_SECTION = "getClassSection"

    // CALENDAR
    const val GET_CALENDAR_CLASS = "getCalendarClass"
    const val GET_CALENDAR_CLASS_SECTION = "getCalendarClassSection"
    const val GET_COMPONENT_CLASS = "getComponentClass"
    const val GET_COMPONENT_CLASS_SECTION = "getComponentClassSection"

    // CALENDAR TERM
    const val GET_CALENDAR_TERMS = "getCalendarTerms"
    const val GET_CALENDAR_TERM = "getCalendarTerm"
}
