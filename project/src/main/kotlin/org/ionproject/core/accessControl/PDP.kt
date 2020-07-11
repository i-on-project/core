package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.interceptors.Request
import org.ionproject.core.common.interceptors.ResourceIdentifierDescriptor
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import javax.annotation.Resource

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class PDP {

    private val cache: AccessControlCache = AccessControlCache()

    /**
     * Authentication path when authorization header is present and access_token query parameter is not.
     */
    fun evaluateAuthorization(
        token: TokenEntity,
        requestDescriptor: Request,
        scope: String,
        authMode: String
    ): TokenEntity {

        //Check if the token is revoked
        if (!token.isValid) {
            logger.info(
                LogMessages.forAuthErrorDetail(
                    authMode,
                    token.hash,
                    requestDescriptor.method,
                    requestDescriptor.path,
                    LogMessages.revokedToken
                )
            )

            throw UnauthenticatedUserException(LogMessages.tokenRevokedMessage)
        }

        //Check if the token is expired
        if (System.currentTimeMillis() > token.expiresAt) {
            logger.info(
                LogMessages.forAuthErrorDetail(
                    authMode,
                    token.hash,
                    requestDescriptor.method,
                    requestDescriptor.path,
                    LogMessages.tokenExpired
                )
            )
            throw UnauthenticatedUserException(LogMessages.tokenExpiredMessage)
        }

        //Check permissions
        if (checkPolicies(token, scope, requestDescriptor, authMode))
            return token
        else {
            logger.info(
                LogMessages.forAuthErrorDetail(
                    LogMessages.tokenHeaderAuth,
                    token.hash,
                    requestDescriptor.method,
                    requestDescriptor.path,
                    LogMessages.lackPrivileges
                )
            )
            throw ForbiddenActionException(LogMessages.lackOfPrivilegesMessage)
        }
    }


    /**
     * Check if the user is allowed to do the action he requested
     */
    private fun checkPolicies(
        token: TokenEntity,
        scope: String,
        requestDescriptor: Request,
        authMode: String
    ): Boolean {

        val policies = cache.getPolicies(scope, requestDescriptor.resourceIdentifier.version)

        val policy = matchResources(requestDescriptor.resourceIdentifier, policies)
        if (policy != null) {
            //A path matched with the request, check the associated HTTP method
            val methods = policy.method

            //Check if requested method is valid
            if (methods.contains(requestDescriptor.method)) {

                logger.info(
                    LogMessages.forAuthSuccess(
                        authMode,
                        token.hash,
                        requestDescriptor.method,
                        requestDescriptor.path
                    )
                )

                return true
            }
        }

        return false
    }


    /**
     * Returns the policy that matches, if any matches
     */
    private fun matchResources(
        resourceIdentifier: ResourceIdentifierDescriptor,
        policies: List<PolicyEntity>
    ): PolicyEntity? {
        for (policy in policies) {
            if (policy.resource == resourceIdentifier.resource && policy.version == resourceIdentifier.version)
                return policy
        }

        return null
    }
}