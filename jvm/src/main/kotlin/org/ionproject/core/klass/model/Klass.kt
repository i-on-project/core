package org.ionproject.core.klass.model

import org.ionproject.core.classSection.ClassSection

/**
 * Reduced representation of a Class.
 * This contains only enough information to uniquely identify the Class
 */
open class Klass(
    val id: Int,
    val courseId: Int,
    val courseAcr: String?, // optional
    val calendarTerm: String
)

/**
 * Fully detailed representation of a Class.
 */
class FullKlass(
    id: Int,
    courseId: Int,
    courseAcr: String?, // optional
    calendarTerm: String,
    val sections: List<ClassSection>
) : Klass(id, courseId, courseAcr, calendarTerm)
