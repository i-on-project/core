package org.ionproject.core.common.transaction

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

/**
 *  Singleton object, holder of the dataSource, giver of connections
 */
@Component
object DataSourceHolder {
    val dataSource: DataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("DBHOST_ION"))
        user = System.getenv("DBUSER_ION")
        password = System.getenv("DBPASSWORD_ION")
    }
}
