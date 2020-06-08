package org.ionproject.core.access_control

import org.ionproject.core.access_control.pap.AuthRepoImpl
import org.ionproject.core.access_control.pap.Policy
import org.ionproject.core.access_control.pap.TokenEntity
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.interceptors.Request
import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManagerImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.util.UriTemplate
import java.net.URI

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class PDP {
  companion object {
    private val pap: AuthRepoImpl = AuthRepoImpl(TransactionManagerImpl(DataSourceHolder))

    fun evaluateRequest(tokenReference: String, requestDescriptor: Request): Boolean {
      val tokenTable = pap.getTableToken(tokenReference)

      /**
       * Checks if the token exists or is valid (not revoked)
       */
      if(tokenTable == null || !tokenTable.isValid) {
        logger.info("Access to ${requestDescriptor.method} was NOT AUTHORIZED on ${requestDescriptor.resource}")
        throw UnauthenticatedUserException("Token is not valid (revoked or unexistent)... try requesting a new one.")
      }

      return checkPolicies(tokenTable, requestDescriptor)
    }

    /**
     * Checks if the user is allowed to do the action he is
     * trying to do.
     */
    fun checkPolicies(tokenTable: TokenEntity, requestDescriptor: Request): Boolean {
      /**
       * Check if the token is not expired.
       */
      if( System.currentTimeMillis() > tokenTable.claims.exp) {
        logger.info("Client_id:${tokenTable.claims.client_id} was NOT AUTHORIZED to ${requestDescriptor.method} on ${requestDescriptor.resource}")
        throw UnauthenticatedUserException("Token expired... try requesting a new one.")
      }

      val policies = pap.getPolicies(tokenTable.claims.scope, requestDescriptor.apiVersion)

      /**
       * Checks if the scope associated has the correct method
       * permissions to the specific path.
       * e.g. ALLOW "GET /v0/courses"
       */
      val policy = matchPaths(requestDescriptor.resource, policies)
      if(policy != null) {
        //A path matched with the request, check the associated HTTP method
        val methods = policy.method
        if(methods.contains(requestDescriptor.method)) {
          logger.info("Client_id:${tokenTable.claims.client_id} was AUTHORIZED to ${requestDescriptor.method} on ${requestDescriptor.resource}")
          return true
        }
      }

      /**No path match, or not the correct method or no policies associated with the scope
       * therefore user has no permission to access that resource
       */
      logger.info("Client_id:${tokenTable.claims.client_id} was NOT AUTHORIZED to ${requestDescriptor.method} on ${requestDescriptor.resource}")
      throw ForbiddenActionException("You Require higher privileges to do that.")
    }

    /**
     * Tries to find a match to the request URI with the paths in the policies list
     * e.g. tries to match:
     * "/v0/courses/5" with "/v0/courses/{id}"
     *
     * If there is a match, it returns the associated policy for further processing
     * if there is no match it means that the scope used has no access to that resource.
     */
    fun matchPaths(path: String, policies: List<Policy>): Policy? {
      val uriRequest = path.replace("/", " ").trim().split(" ")

      for(policy in policies) {
        val policyUri = policy.path.replace("/", " ").trim().split(" ")

        if(uriRequest.size != policyUri.size)
          continue
        else {
          var i = 0
          var match = true

          for (segment in policyUri) {
            if (segment == "?" || segment == uriRequest[i]) {
              i++
            } else {
              match = false
              break
            }
          }

          if(match)
            return policy
        }
      }

      return null
    }
  }
}