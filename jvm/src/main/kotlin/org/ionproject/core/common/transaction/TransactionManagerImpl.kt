package org.ionproject.core.common.transaction

import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.jdbi.v3.core.ConnectionException
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.transaction.SerializableTransactionRunner
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TransactionManagerImpl(dsh: DataSourceHolder) : TransactionManager {
    /**
     * Jdbi instance wraps a JDBC DataSource
     */
    private val jdbi: Jdbi = Jdbi.create(dsh.dataSource).apply {
        installPlugin(KotlinPlugin())
        installPlugin(SqlObjectPlugin())
        transactionHandler = SerializableTransactionRunner()
        // setSqlLogger(SqlLogger()) // uncomment this line to see what request are being sent to the database
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TransactionManagerImpl::class.java)
    }

    /**
     * Executes the transaction passed as parameter with the
     * isolation level specified against the database
     * configured by DataSource.
     * The object `jdbi` contains a factory of connections
     * to the database.
     */
    override fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R): R {
        try {
            return jdbi.inTransaction<R, Exception>(isolationLevel) {
                transaction(it)
            }
        } catch (e: ConnectionException) {
            logger.error(e.localizedMessage)
            throw InternalServerErrorException("Was not possible to establish a database connection")
        } catch (e: Exception) {
            logger.error(e.localizedMessage)
            throw e
        }
    }
}
