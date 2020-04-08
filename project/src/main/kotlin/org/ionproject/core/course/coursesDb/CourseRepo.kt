package org.ionproject.core.course.coursesDb

import org.ionproject.core.common.model.Course

interface CourseRepo {
    fun getCourses(): List<Course>
    fun getCourseById(id: Int): Course?
}