package org.ionproject.core.common.transaction

import org.ionproject.core.common.Logger
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Component

@Component
class TransactionManagerImpl(dsh: DataSourceHolder) : TransactionManager {
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

    /**TODO: EXCEPTION HANDLING?
     * For now it returns NULL in case of exception,
     * If throwed the exception all repo methods would need a try,catch
     * For sake of simplicity and ease of read for now it only returns null
     *
     * A better solution maybe would be to have 2 methods, 1 for read operations (SELECT)
     * and 1 for write (INSERT, UPDATE...) the read method can return NULL in case of exception
     * as the only cause is notFound the write method could return a `Result` type to encapsulate the possible
     * exception (bad request, duplicate...) or correct result.
     *
     * Also the current way of using transactionManager leads to duplicate
     * sql code, as it not allows to share the handle between multiple operations
     * in the repository.
     */
    override fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R): R? {
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
            Logger.logError(e.localizedMessage)
            return null     //TODO: THIS SHOULD BE REPLACED BY A THROWN EXCEPTION OR IT WONT CATCH EXCEPTIONS INSIDE THE LAMBDA
        } finally {
            handle?.close()
        }
    }

}