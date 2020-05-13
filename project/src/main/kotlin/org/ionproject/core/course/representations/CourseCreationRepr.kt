package org.ionproject.core.course.representations

import com.fasterxml.jackson.annotation.JsonCreator
import org.ionproject.core.course.model.Course

class InvalidCourseException : Exception()

class CourseInputModel @JsonCreator constructor(
  val courseId: Int,
  val acronym: String,
  val name: String,
  val term: String
) {
  fun toCourse() = Course(courseId, acronym, name, term)
}
