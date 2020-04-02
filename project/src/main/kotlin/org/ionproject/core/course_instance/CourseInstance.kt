package org.ionproject.core.course_instance

import com.fasterxml.jackson.annotation.JsonIgnore
import org.ionproject.core.class_section.ClassSection

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
    @JsonIgnore val sections: List<ClassSection>) : CourseInstance(course, calendarTerm)