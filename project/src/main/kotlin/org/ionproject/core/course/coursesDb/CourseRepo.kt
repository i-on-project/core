package org.ionproject.core.course.coursesDb

import org.ionproject.core.common.mappers.CourseMapper
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.common.transaction.ITransactionManager
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Component

@Component
class CourseRepo(private val tm : ITransactionManager) : ICourseRepo {
    val courseMapper : CourseMapper = CourseMapper()

    override fun getCourses(): List<ICourse> {
        return tm.run(TransactionIsolationLevel.NONE) {
            handle -> handle.createQuery("SELECT * FROM Course")
                .map(courseMapper)
                .list()
        }
    }

    override fun getCourseByAcr(acr: String): ICourse? {
        return tm.run(TransactionIsolationLevel.NONE) {
            handle -> handle.createQuery("SELECT * FROM Course WHERE acronym= :acr")
                .bind("acr", acr)
                .map(courseMapper)
                .one()
        }
    }
}