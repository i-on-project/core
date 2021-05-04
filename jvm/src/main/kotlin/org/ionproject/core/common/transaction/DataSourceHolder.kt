package org.ionproject.core.common.transaction

import org.ionproject.core.common.customExceptions.EnvironmentVariableNotFoundException
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.sql.DataSource

private const val JDBC_ENVIRONMENT_VARIABLE = "JDBC_DATABASE_URL"
private val log = LoggerFactory.getLogger(DataSourceHolder::class.java)

@Component
object DataSourceHolder {
    val dataSource: DataSource = PGSimpleDataSource().apply {
        val url: String = System.getenv(JDBC_ENVIRONMENT_VARIABLE)
            ?: throw EnvironmentVariableNotFoundException(JDBC_ENVIRONMENT_VARIABLE)

        log.debug("Using $JDBC_ENVIRONMENT_VARIABLE={}", url)
        setUrl(url)
    }
}
