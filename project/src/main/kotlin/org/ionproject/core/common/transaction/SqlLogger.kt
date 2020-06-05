package org.ionproject.core.common.transaction

import org.jdbi.v3.core.statement.SqlLogger
import org.jdbi.v3.core.statement.StatementContext

class SqlLogger : SqlLogger {
    override fun logBeforeExecution(context: StatementContext) {
        println("STATEMENT = ${context.statement}")
    }
}