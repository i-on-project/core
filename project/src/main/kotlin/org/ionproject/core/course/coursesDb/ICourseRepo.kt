package org.ionproject.core.course.coursesDb

import org.ionproject.core.common.modelInterfaces.ICourse

interface ICourseRepo {
    fun getCourses(): List<ICourse>
    fun getCourseByAcr(acr: String): ICourse?
}