package org.ionproject.core

import org.ionproject.core.common.Media
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Primary
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
internal class ControllerTester {
    @Autowired
    lateinit var mocker: MockMvc

    fun isValidSiren(uri: URI) = mocker.get(uri) {
        accept = Media.MEDIA_SIREN
    }.andExpect {
        status { isOk }
        content { contentType("application/vnd.siren+json") }
        jsonPath("$.links") { exists() }
        jsonPath("$.actions") { exists() }
    }.andReturn()
}

@Primary
class TransactionManagerTest : TransactionManager {

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
}