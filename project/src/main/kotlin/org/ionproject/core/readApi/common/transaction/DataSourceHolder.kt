package org.ionproject.core.readApi.common.transaction

import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.sql.DataSource

private val log = LoggerFactory.getLogger(DataSourceHolder::class.java)

/**
 *  Singleton object, holder of the dataSource, giver of connections
 */
@Component
object DataSourceHolder {
    val dataSource: DataSource = PGSimpleDataSource().apply {
        val url: String? = System.getenv("JDBC_DATABASE_URL")
        log.debug("Using JDBC_DATABASE_URL={}", url)
        setUrl(url ?: logAndThrow())
    }

    private fun logAndThrow(): String {
        val msg = "Fatal: environment variable JDBC_DATABASE_URL is not defined."
        log.error(msg)
        throw RuntimeException(msg)
    }
}
