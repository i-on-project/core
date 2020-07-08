package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.DerivedTokenClaims
import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenClaims
import org.ionproject.core.accessControl.pap.entities.TokenEntity
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
    private val tokenGenerator = TokenGenerator()

    /**
     * Evaluates if the access token received as a query parameter is valid
     */
    fun evaluateAccessToken(tokenReference: String, method: String, requestUrl: String) : TokenEntity {

        val tokenBase64Decoded = tokenGenerator.decodeBase64url(tokenReference)
        val tokenHash = tokenGenerator.getHash(tokenBase64Decoded)
        val token = pap.getDerivedTableToken(tokenHash)

        //The order of this operation is before the method check just for extra log info
        if(token == null) {
            logger.info(LogMessages.forErrorImport(method, requestUrl, "TOKEN DOESN'T EXIST"))
            throw UnauthenticatedUserException("Invalid access, access_token not found.")
        }

        val claims = token.claims as DerivedTokenClaims

        //Check method - this type of access doesn't allow UNSAFE METHODS (POST, PUT...)
        if(method != "GET" && method != "HEAD") {
            logger.info(LogMessages.forErrorImportDetail(claims.derivedTokenReference, claims.fatherTokenHash, method,
                requestUrl, "UNSAFE METHOD ATTEMPT ON IMPORT URL"))
            throw BadRequestException("This type of access doesn't allow unsafe methods (PUT, POST...).")
        }


        //Check if token is not REVOKED
        if(!token.isValid) {
            logger.info(LogMessages.forErrorImportDetail(claims.derivedTokenReference, claims.fatherTokenHash, method,
                requestUrl, "TOKEN REVOKED"))

            throw UnauthenticatedUserException("Token Revoked, try requesting another import link.")
        }

        //Check if token is not expired
        val currTime = System.currentTimeMillis()
        if(currTime > token.expiresAt) {
            logger.info(LogMessages.forErrorImportDetail(claims.derivedTokenReference, claims.fatherTokenHash, method,
                requestUrl, "TOKEN EXPIRED"))

            throw UnauthenticatedUserException("Token expired, try requesting another import link.")
        }

        //Check if claim URL matches with request URL
        val tokenUrl = (token.claims as DerivedTokenClaims).uri
        if(tokenUrl != requestUrl) {
            logger.info(LogMessages.forErrorImportDetail(claims.derivedTokenReference, claims.fatherTokenHash, method,
                requestUrl, "PROVIDED TOKEN ONLY PROVIDES ACCESS TO $tokenUrl"))
            throw ForbiddenActionException("The presented token does not grant access to the current location. " +
                "<EXPECTED:${tokenUrl}> | <ACTUAL:${requestUrl}>")
        }

        return token
    }

    fun evaluateRequest(tokenHash: String, requestDescriptor: Request): TokenEntity {
        val token = pap.getTableToken(tokenHash)

        //Check if the token exists
        if (token == null) {
            logger.info(LogMessages.forError(requestDescriptor.method, requestDescriptor.path, "INEXISTENT TOKEN"))
            throw UnauthenticatedUserException("Inexistent token... try requesting a new one.")
        }

        //Check if the token is revoked
        if(!token.isValid) {
            logger.info(LogMessages.forErrorDetail(token.hash, requestDescriptor.method, requestDescriptor.path,
                "REVOKED TOKEN"))
            throw UnauthenticatedUserException("Token Revoked... try requesting a new one.")
        }

         if(checkPolicies(token, requestDescriptor))
             return token
        else {
             logger.info(LogMessages.forErrorDetail(token.hash, requestDescriptor.method, requestDescriptor.path,
                 "LACK OF PRIVILEGES"))
             throw ForbiddenActionException("You Require higher privileges to do that.")
         }
    }

    /**
     * Check if the user is allowed to do the action he requested
     */
    private fun checkPolicies(token: TokenEntity, requestDescriptor: Request) : Boolean {
        //Checks if the token is expired
        val tokenClaims = token.claims as TokenClaims

        if (System.currentTimeMillis() > token.expiresAt) {
            logger.info(LogMessages.forErrorDetail(token.hash, requestDescriptor.method, requestDescriptor.path, "TOKEN EXPIRED"))
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
                logger.info(LogMessages.forSuccess(requestDescriptor.method, requestDescriptor.path))
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