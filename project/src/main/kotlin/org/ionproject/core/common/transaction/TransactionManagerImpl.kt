package org.ionproject.core.common.transaction

import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.jdbi.v3.core.ConnectionException
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class TransactionManagerImpl(dsh: DataSourceHolder) : TransactionManager {
  /**
   * Jdbi instance wraps a JDBC DataSource
   */
  private val jdbi: Jdbi = Jdbi.create(dsh.dataSource)

  /**
   * Executes the transaction passed as parameter with the
   * isolation level specified against the database
   * configured by DataSource.
   * The object `jdbi` contains a factory of connections
   * to the database.
   */
  override fun <R> run(isolationLevel: TransactionIsolationLevel, transaction: (Handle) -> R): R {
    var handle: Handle? = null
    try {
      handle = jdbi.open() //Obtaining a handle wrapper to the datasource

      handle.begin() //Initiates the transaction
      handle.setTransactionIsolation(isolationLevel)
      val result = transaction(handle) //Executing transaction code

      handle.commit()
      return result
    } catch (e: ConnectionException) {
      logger.error(e.localizedMessage)
      throw InternalServerErrorException("Was not possible to establish a database connection")
    } catch (e: Exception) {
      handle?.rollback()
      logger.error(e.localizedMessage)
      throw e // propagate
    } finally {
      handle?.close()
    }
  }

}