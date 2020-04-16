package org.ionproject.core.common.model

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Reduced representation of a Class.
 * This contains only enough information to uniquely identify the Class
 */
open class Klass(
        val courseId: Int,
        val courseAcr: String?, // optional
        val calendarTerm: String)

/**
 * Fully detailed representation of a Class.
 */
class FullKlass(
        courseId: Int,
        courseAcr: String?, // optional
        calendarTerm: String,
        @JsonIgnore val sections: List<ClassSection>) : Klass(courseId, courseAcr, calendarTerm)