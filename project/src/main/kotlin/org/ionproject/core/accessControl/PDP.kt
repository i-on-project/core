package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.DerivedTokenClaims
import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenClaims
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.pap.sql.AuthRepoImpl
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
    private val tokenGenerator = TokenGenerator()

    /**
     * Evaluates if the access token received as a query parameter is valid
     */
    fun evaluateAccessToken(tokenReference: String, method: String, requestUrl: String) {
        //Check method - this type of access doesn't allow UNSAFE METHODS (POST, PUT...)
        if(method != "GET" && method != "HEAD") {
            logger.info("An access_token query parameter authentication method failed: INVALID METHOD \"$method\" ")
            throw BadRequestException("This type of access doesn't allow unsafe methods (PUT, POST...).")
        }

        //Payload object
        //Transforms the base64url encoded value to the SHA-256 hashed value
        val tokenBase64Decoded = tokenGenerator.decodeBase64url(tokenReference)
        val tokenHash = tokenGenerator.getHash(tokenBase64Decoded)
        val tokenObj = pap.getDerivedTableToken(tokenHash)

        if(tokenObj == null) {
            logger.info("An access_token query parameter authentication method failed: TOKEN NOT FOUND")
            throw UnauthenticatedUserException("Invalid access_token, not found.")
        }

        //Check if token is not expired
        if(!tokenObj.isValid) {
            logger.info("An access_token query parameter authentication method failed: TOKEN REVOKED")
            throw UnauthenticatedUserException("Token Revoked, try requesting another import link.")
        }

        //Check if token is not expired
        val currTime = System.currentTimeMillis()
        if(currTime > tokenObj.expiresAt) {
            logger.info("An access_token query parameter authentication method failed: TOKEN EXPIRED")
            throw UnauthenticatedUserException("Token expired, try requesting another import link.")
        }

        //Check if claim URL matches with request URL
        val tokenUrl = (tokenObj.claims as DerivedTokenClaims).uri
        if(tokenUrl != requestUrl) {
            logger.info("An access_token query parameter authentication method failed: INVALID ACCESS the presented " +
                "token does not grant access to the current location. <EXPECTED:${tokenUrl}> | <ACTUAL:${requestUrl}>\"")
            throw ForbiddenActionException("The presented token does not grant access to the current location. " +
                "<EXPECTED:${tokenUrl}> | <ACTUAL:${requestUrl}>")
        }

    }

    fun evaluateRequest(tokenHash: String, requestDescriptor: Request): Boolean {
        val token = pap.getTableToken(tokenHash)

        //Check if the token exists
        if (token == null) {
            logger.info("An AUTHORIZATION HEADER authentication method failed " +
                "(${requestDescriptor.method} on ${requestDescriptor.path}): INEXISTENT TOKEN")
            throw UnauthenticatedUserException("Inexistent token... try requesting a new one.")
        }

        //Check if the token is revoked
        if(!token.isValid) {
            logger.info("An AUTHORIZATION HEADER authentication method failed " +
                "(${requestDescriptor.method} on ${requestDescriptor.path}): REVOKED TOKEN")
            throw UnauthenticatedUserException("Token Revoked... try requesting a new one.")
        }

        return checkPolicies(token, requestDescriptor)
    }

    /**
     * Check if the user is allowed to do the action he requested
     */
    private fun checkPolicies(token: TokenEntity, requestDescriptor: Request) : Boolean {
        //Checks if the token is expired
        val tokenClaims = token.claims as TokenClaims

        if (System.currentTimeMillis() > token.expiresAt) {
            logger.info("An AUTHORIZATION HEADER authentication method failed " +
                "(${requestDescriptor.method} on ${requestDescriptor.path}): TOKEN EXPIRED")
            throw UnauthenticatedUserException("Token expired... try requesting a new one.")
        }

        val policies = pap.getPolicies(tokenClaims.scope, requestDescriptor.resourceIdentifier.version)

        /**
         * Checks if the scope associated has the correct method permissions to the specific path.
         * e.g. ALLOW "GET /v0/courses"
         */
        val policy = matchResources(requestDescriptor.resourceIdentifier, policies)
        if (policy != null) {
            //A path matched with the request, check the associated HTTP method
            val methods = policy.method
            if (methods.contains(requestDescriptor.method)) {
                logger.info("An AUTHORIZATION HEADER authentication method succeeded (${requestDescriptor.method} on ${requestDescriptor.path})")
                return true
            }
        }

        /**No path match, or not the correct method or no policies associated with the scope
         * therefore user has no permission to access that resource
         */
        logger.info("An AUTHORIZATION HEADER authentication method failed " +
            "(${requestDescriptor.method} on ${requestDescriptor.path}): LACK OF PRIVILEGES")
        throw ForbiddenActionException("You Require higher privileges to do that.")
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