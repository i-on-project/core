package org.ionproject.core.course

import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.course.coursesDb.ICourseRepo
import org.springframework.stereotype.Component

@Component
class CourseServices(private val repo : ICourseRepo) {
    fun getCourses(): List<ICourse> {
        return repo.getCourses()
    }

    fun getCourseByAcr(acr: String): ICourse? {
        return repo.getCourseByAcr(acr)
    }


}