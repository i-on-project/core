package org.ionproject.core.common.transaction

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.transaction.TransactionIsolationLevel

interface ITransactionManager {
    fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R) : R

}