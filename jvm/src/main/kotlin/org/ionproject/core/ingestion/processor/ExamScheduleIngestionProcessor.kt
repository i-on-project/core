package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.ExamSchedule
import org.slf4j.LoggerFactory

class ExamScheduleIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<ExamSchedule> {

    companion object {
        private val log = LoggerFactory.getLogger(ExamScheduleIngestionProcessor::class.java)
    }

    override fun process(data: ExamSchedule) {
    }
}
