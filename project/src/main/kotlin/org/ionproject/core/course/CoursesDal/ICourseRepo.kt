package org.ionproject.core.course.CoursesDal

import org.ionproject.core.common.modelInterfaces.ICourse

interface ICourseRepo {
    fun readCourses(): List<ICourse>
    fun readCourse(acr: String): ICourse
}