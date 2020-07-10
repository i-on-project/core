package org.ionproject.core.utils

import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles

class TransactionManagerImpl(dsh: DataSourceHolder) : TransactionManager {

    private val jdbi: Jdbi = Jdbi.create(dsh.dataSource)

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
