package org.ionproject.core.utils

import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel

class TransactionManagerImpl(dsh: DataSourceHolder) : TransactionManager {

    private val jdbi: Jdbi = Jdbi.create(dsh.getDataSource())

    override fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R): R {
        jdbi.open().use {
            it.begin()
            it.setTransactionIsolation(isolationLevel)
            val res = transaction(it)
            it.rollback()
            return res
        }
    }
}
