package org.ionproject.core.course.representations

import com.fasterxml.jackson.annotation.JsonCreator
import org.ionproject.core.common.model.Course

class InvalidCourseException : Exception()

class CourseInputModel @JsonCreator constructor(val courseId: Int,
                                                val acronym : String,
                                                val name : String) {
    fun toCourse() = Course(courseId, acronym, name) ?: throw InvalidCourseException()
}
