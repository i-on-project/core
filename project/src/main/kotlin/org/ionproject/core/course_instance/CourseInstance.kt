package org.ionproject.core.course_instance

/**
 * Reduced representation of a Class.
 * This contains only enough information to uniquely identify the Class
 */
open class CourseInstance(
    val course: String,
    val calendarTerm: String)

/**
 * Fully detailed representation of a Class.
 */
class FullCourseInstance(
    course: String,
    calendarTerm: String,
    val name: String) : CourseInstance(course, calendarTerm)