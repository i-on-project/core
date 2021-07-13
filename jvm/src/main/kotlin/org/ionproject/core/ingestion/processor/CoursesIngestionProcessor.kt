package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.SchoolCourses

@FileIngestion("courses", true)
class CoursesIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<SchoolCourses> {

    override fun process(data: SchoolCourses) {
        TODO("Not yet implemented")
    }

}