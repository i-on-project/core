package org.ionproject.core.course

import org.ionproject.core.course.model.Course

interface CourseRepo {
    fun getCourses(page: Int, limit: Int): List<Course>
    fun getCourseById(id: Int): Course?
}
