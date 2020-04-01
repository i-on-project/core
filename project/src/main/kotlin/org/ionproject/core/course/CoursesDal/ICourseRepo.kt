package org.ionproject.core.course.CoursesDal

import org.ionproject.core.common.model.Course

interface ICourseRepo {
    fun readCourses(): List<Course>
    fun readCourse(acr: String): Course
}