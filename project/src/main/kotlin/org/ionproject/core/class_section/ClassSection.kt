package org.ionproject.core.class_section

/**
 * Reduced representation of a Class Section.
 * This contains only enough information to uniquely identify each Class Section.
 */
open class ClassSection(
    val course: String,
    val calendarTerm: String,
    val id: String)

/**
 * Fully detailed representation of a Class Section.
 */
class FullClassSection(
    course: String,
    calendarTerm: String,
    id: String) : ClassSection(course, calendarTerm, id)
