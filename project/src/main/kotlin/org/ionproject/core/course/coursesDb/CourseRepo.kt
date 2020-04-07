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

    //TODO: UNABLE TO SET ISOLATION LEVEL TO 0 ERROR

    override fun getCourses(): List<ICourse> {
        return tm.run(TransactionIsolationLevel.READ_COMMITTED) {
            handle -> handle.createQuery("SELECT * FROM dbo.Course")
                .map(courseMapper)
                .list()
        }
    }

    override fun getCourseByAcr(acr: String): ICourse? {
        return tm.run(TransactionIsolationLevel.READ_COMMITTED) {
            handle -> handle.createQuery("SELECT * FROM dbo.Course WHERE acronym= :acr")
                .bind("acr", acr)
                .map(courseMapper)
                .one()
        }
    }
}