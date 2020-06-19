package org.ionproject.core.readApi.common.transaction

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.transaction.TransactionIsolationLevel

interface TransactionManager {
    fun <R> run(
        isolationLevel: TransactionIsolationLevel = TransactionIsolationLevel.READ_COMMITTED,
        transaction: (Handle) -> R
    ): R
}