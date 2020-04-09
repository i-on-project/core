package org.ionproject.core

import org.ionproject.core.common.transaction.ITransactionManager
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Primary
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
internal class ControllerTester {
    @Autowired
    lateinit var mocker: MockMvc
}

@Primary
class TransactionManagerTest : ITransactionManager {

    private val jdbi: Jdbi = Jdbi.create(PGSimpleDataSource().apply {
        setUrl(System.getenv("DBHOST_ION_TESTS"))
        user = System.getenv("DBUSER_ION_TESTS")
        password = System.getenv("DBPASSWORD_ION_TESTS")
    })

    override fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R): R {
        jdbi.open().use {
            it.begin()
            it.setTransactionIsolation(isolationLevel)
            val res = transaction(it)
            it.rollback()
            return res
        }
    }

    override fun <R> run(transaction: (Handle) -> R): R = run(TransactionIsolationLevel.NONE, transaction)
}