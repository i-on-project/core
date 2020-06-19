package org.ionproject.core.readApi.course

import org.ionproject.core.readApi.course.model.Course

interface CourseRepo {
    fun getCourses(page: Int, limit: Int): List<Course>
    fun getCourseById(id: Int): Course?
}