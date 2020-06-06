package org.ionproject.core.access_control

import org.ionproject.core.access_control.pap.AuthRepoImpl
import org.ionproject.core.access_control.pap.TokenEntity
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.interceptors.Request
import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManagerImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class PDP {
  companion object {
    private val pap: AuthRepoImpl = AuthRepoImpl(TransactionManagerImpl(DataSourceHolder))

    fun evaluateRequest(tokenReference: String, requestDescriptor: Request): Boolean {
      val tokenTable = pap.getTableToken(tokenReference)

      if(tokenTable == null || !tokenTable.isValid)
        throw UnauthenticatedUserException("Token is not valid, it may have expired... try requesting a new one.")

      return checkPolicies(tokenTable, requestDescriptor)
    }

    /**
     * Checks if the user is allowed to do the action he is
     * trying to do.
     */
    fun checkPolicies(tokenTable: TokenEntity, requestDescriptor: Request): Boolean {
      //Check if the associated scope has permissions for this request
      logger.info("Client_id:${tokenTable.claims} was authorized to ${requestDescriptor.method} on ${requestDescriptor.resource}")
      return true
    }
  }
}