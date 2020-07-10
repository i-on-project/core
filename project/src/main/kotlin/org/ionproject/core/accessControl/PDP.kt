package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.*
import org.ionproject.core.accessControl.pap.sql.AuthRepoImpl
import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.interceptors.Request
import org.ionproject.core.common.interceptors.ResourceIdentifierDescriptor
import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManagerImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class PDP {
    private val pap: AuthRepoImpl = AuthRepoImpl(TransactionManagerImpl(DataSourceHolder))

    /**
     * Authentication path when access_token query parameter is present
     * (typically import url's, but some user can try to reuse the token somewhere else)
     */
    fun evaluateAccessTokenAuthentication(
        tokenHash: String,
        requestDescriptor: Request
    ) : TokenEntity {

        val token = pap.getDerivedTableToken(tokenHash)

        //The order of this operation is before the method check just for extra log info
        if(token == null) {
            logger.info(
                LogMessages.forAuthError(
                    LogMessages.importUrlAuth,
                    requestDescriptor.method,
                    requestDescriptor.path,
                    LogMessages.inexistentToken
                )
            )

            throw UnauthenticatedUserException(LogMessages.inexistentTokenMessage
            )
        }


        //Check method - this type of access doesn't allow UNSAFE METHODS (POST, PUT...)
        if(requestDescriptor.method != "GET" && requestDescriptor.method != "HEAD") {
            logger.info(
                LogMessages.forAuthErrorDetail(
                    LogMessages.importUrlAuth,
                    token.hash,
                    requestDescriptor.method,
                    requestDescriptor.path,
                    LogMessages.invalidMethod
                )
            )

            throw BadRequestException(LogMessages.unsafeMethodMessage)
        }

        val claims = token.claims as DerivedTokenClaims
        if(checkPolicies(token, claims.scope, requestDescriptor, LogMessages.importUrlAuth))
            return token
        else {
            logger.info(
                LogMessages.forAuthErrorDetail(
                    LogMessages.importUrlAuth,
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
     * Authentication path when authorization header is present and access_token query parameter is not.
     */
    fun evaluateAuthorizationHeaderAuthentication(
        tokenHash: String,
        requestDescriptor: Request
    ): TokenEntity {

        val token = pap.getTableToken(tokenHash)

        //Check if the token exists
        if (token == null) {
            logger.info(
                LogMessages.forAuthError(
                    LogMessages.tokenHeaderAuth,
                    requestDescriptor.method,
                    requestDescriptor.path,
                    LogMessages.inexistentToken
                )
            )
            throw UnauthenticatedUserException(LogMessages.inexistentTokenMessage)
        }

        val claims = token.claims as TokenClaims
         if(checkPolicies(token, claims.scope, requestDescriptor, LogMessages.tokenHeaderAuth))
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
    private fun checkPolicies(token: TokenEntity, scope: String, requestDescriptor: Request, authMode: String) : Boolean {
        //Check if the token is revoked
        if(!token.isValid) {
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

        val policies = pap.getPolicies(scope, requestDescriptor.resourceIdentifier.version)

        /**
         * Checks if the scope associated has the correct method permissions to the specific path.
         * e.g. ALLOW "GET /v0/courses"
         */
        val policy = matchResources(requestDescriptor.resourceIdentifier, policies)
        if (policy != null) {
            //A path matched with the request, check the associated HTTP method
            val methods = policy.method

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
    private fun matchResources(resourceIdentifier: ResourceIdentifierDescriptor, policies: List<PolicyEntity>): PolicyEntity? {
        for (policy in policies) {
            if(policy.resource == resourceIdentifier.resource && policy.version == resourceIdentifier.version)
                return policy
        }

        return null
    }
}