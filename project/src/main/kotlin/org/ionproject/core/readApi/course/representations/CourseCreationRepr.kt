package org.ionproject.core.readApi.course.representations

import com.fasterxml.jackson.annotation.JsonCreator
import org.ionproject.core.readApi.course.model.Course

class InvalidCourseException : Exception()

class CourseInputModel @JsonCreator constructor(
    val courseId: Int,
    val acronym: String,
    val name: String,
    val term: String
) {
    fun toCourse() = Course(courseId, acronym, name, term)
}
