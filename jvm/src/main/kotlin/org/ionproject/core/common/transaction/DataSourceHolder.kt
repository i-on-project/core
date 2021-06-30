package org.ionproject.core.common.transaction

import org.ionproject.core.common.customExceptions.EnvironmentVariableNotFoundException
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DataSourceHolder {

    companion object {
        private const val JDBC_ENVIRONMENT_VARIABLE = "JDBC_DATABASE_URL"
        private val logger = LoggerFactory.getLogger(DataSourceHolder::class.java)
    }

    @Primary
    @Bean
    fun getDataSource(): DataSource = PGSimpleDataSource().apply {
        val url: String = System.getenv(JDBC_ENVIRONMENT_VARIABLE)
            ?: throw EnvironmentVariableNotFoundException(JDBC_ENVIRONMENT_VARIABLE)

        logger.debug("Using $JDBC_ENVIRONMENT_VARIABLE={}", url)
        setUrl(url)
    }
}
