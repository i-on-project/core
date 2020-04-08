package org.ionproject.core.common.transaction

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

/**
 *  Singleton object, holder of the dataSource, giver of connections
 */

const val URL_VAR = "DBHOST_ION_TEST"   //To choose between test Db or Production db, choose the correct URL var
const val HOST_VAR = "DBUSER"
const val PASSWORD_VAR = "DBPASSWORD"

@Component
object DataSourceHolder {
   val dataSource : DataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv(URL_VAR)) 
        user = System.getenv(HOST_VAR)
        password = System.getenv(PASSWORD_VAR)
    }
}
