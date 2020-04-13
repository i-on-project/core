package org.ionproject.core.common.transaction

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Component

@Component
class TransactionManager(dsh: DataSourceHolder) : ITransactionManager {
    /**
     * Jdbi instance wraps a JDBC DataSource
     */
    private val jdbi: Jdbi = Jdbi.create(dsh.dataSource)

    /**
     * Executes the transaction passed as parameter with the
     * isolation level specified against the database
     * configured by DataSource.
     * The object `jdbi` contains a factory of connections
     * to the database.
     */

    //TODO: EXCEPTIONS HANDLING?
    override fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R): R {
        var handle: Handle? = null
        try {
            handle = jdbi.open() //Obtaining a handle wrapper to the datasource

            handle.begin() //Initiates the transaction
            handle.setTransactionIsolation(isolationLevel)
            val result = transaction(handle) //Executing transaction code

            handle.commit()
            return result
        } catch (e: Exception) {
            handle?.rollback()
            throw e
        } finally {
            handle?.close()
        }
    }

    override fun <R> run(transaction: (Handle) -> R): R = run(TransactionIsolationLevel.NONE, transaction)
}