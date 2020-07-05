package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.pap.sql.AuthRepoImpl
import org.ionproject.core.accessControl.representations.JWTPayloadRepr
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
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
    private val pap: AuthRepoImpl = AuthRepoImpl(TransactionManagerImpl(DataSourceHolder))
    private val tokenGenerator = TokenGenerator()

    /**
     * Evaluates if the access token received as a query parameter is valid
     */
    fun evaluateJwtToken(jwt: String, method: String, requestUrl: String) {
        val parts = jwt.split(".")

        //Check method - this type of access doesn't allow UNSAFE METHODS (POST, PUT...)
        if(method != "GET" && method != "HEAD") {
            logger.info("An access_token query parameter authentication method failed: INVALID METHOD \"$method\" ")
            throw BadRequestException("This type of access doesn't allow unsafe methods (PUT, POST...).")
        }

        //Check token structure
        if(parts.size != 3) {
            logger.info("An access_token query parameter authentication method failed: " +
                "INVALID TOKEN STRUCTURE \"$jwt\" ")
            throw BadRequestException("The presented token is invalid " +
                "(missing a component [HEADER].[PAYLOAD].[SIGNATURE]).")
        }

        //Check token signature
        if(!tokenGenerator.verifySignatureJWT(jwt)) {
            logger.info("An access_token query parameter authentication method failed: INVALID SIGNATURE \"$jwt\" ")
            throw UnauthenticatedUserException("Invalid Token (signature check failed).")

        }

        //Payload object
        val jwtPayload = decodeJwtPayload(jwt)

        //Check if token is not expired
        val currTime = System.currentTimeMillis()
        if(currTime > jwtPayload.exp) {
            logger.info("An access_token query parameter authentication method failed: TOKEN EXPIRED")
            throw UnauthenticatedUserException("Token expired, try requesting another import link.")
        }

        //Check if claim URL matches with request URL
        if(jwtPayload.url != requestUrl) {
            logger.info("An access_token query parameter authentication method failed: INVALID ACCESS the presented " +
                "token does not grant access to the current location. <EXPECTED:${jwtPayload.url}> | <ACTUAL:${requestUrl}>\"")
            throw ForbiddenActionException("The presented token does not grant access to the current location. " +
                "<EXPECTED:${jwtPayload.url}> | <ACTUAL:${requestUrl}>")
        }

    }

    private fun decodeJwtPayload(jwt: String) : JWTPayloadRepr {
        val parts = jwt.split(".")
        val payloadJson = tokenGenerator.decodeBase64url(parts[1])

        val mapper = jacksonObjectMapper()
        return mapper.readValue(payloadJson, JWTPayloadRepr::class.java)
    }

    fun evaluateRequest(tokenHash: String, requestDescriptor: Request): Int {
        val token = pap.getTableToken(tokenHash)

        //Check if the token is valid or exists
        if (token == null || !token.isValid) {
            logger.info("Access to ${requestDescriptor.method} was NOT AUTHORIZED on ${requestDescriptor.resource}")
            throw UnauthenticatedUserException("Token is not valid (revoked or unexistent)... try requesting a new one.")
        }

        return checkPolicies(token, requestDescriptor)
    }

    /**
     * Check if the user is allowed to do the action he requested
     */
    private fun checkPolicies(token: TokenEntity, requestDescriptor: Request): Int {
        //Checks if the token is expired
        if (System.currentTimeMillis() > token.expiresAt) {
            logger.info("Client_id:${token.claims.client_id} was NOT AUTHORIZED to ${requestDescriptor.method} on ${requestDescriptor.resource}")
            throw UnauthenticatedUserException("Token expired... try requesting a new one.")
        }

        val policies = pap.getPolicies(token.claims.scope, requestDescriptor.apiVersion)

        /**
         * Checks if the scope associated has the correct method permissions to the specific path.
         * e.g. ALLOW "GET /v0/courses"
         */
        val policy = matchPaths(requestDescriptor.resource, policies)
        if (policy != null) {
            //A path matched with the request, check the associated HTTP method
            val methods = policy.method
            if (methods.contains(requestDescriptor.method)) {
                logger.info("Client_id:${token.claims.client_id} was AUTHORIZED to ${requestDescriptor.method} on ${requestDescriptor.resource}")
                return token.claims.client_id
            }
        }

        /**No path match, or not the correct method or no policies associated with the scope
         * therefore user has no permission to access that resource
         */
        logger.info("Client_id:${token.claims.client_id} was NOT AUTHORIZED to ${requestDescriptor.method} on ${requestDescriptor.resource}")
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
    private fun matchPaths(path: String, policies: List<PolicyEntity>): PolicyEntity? {
        val uriRequest = path.replace("/", " ").trim().split(" ")

        for (policy in policies) {
            val policyUri = policy.path.replace("/", " ").trim().split(" ")

            if (uriRequest.size != policyUri.size)
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

                if (match)
                    return policy
            }
        }

        return null
    }
}