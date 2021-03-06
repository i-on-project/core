package org.ionproject.core.common.transaction

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.transaction.TransactionIsolationLevel

interface TransactionManager {
    fun <R> run(
        isolationLevel: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE,
        transaction: (Handle) -> R
    ): R
}
