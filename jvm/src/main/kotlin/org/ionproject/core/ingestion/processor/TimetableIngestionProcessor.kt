package org.ionproject.core.ingestion.processor

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.ingestion.model.Timetable
import org.slf4j.LoggerFactory

class TimetableIngestionProcessor(val tm: TransactionManager) : IngestionProcessor<Timetable> {

    companion object {
        private val log = LoggerFactory.getLogger(TimetableIngestionProcessor::class.java)
    }

    override fun process(data: Timetable) {
    }
}
