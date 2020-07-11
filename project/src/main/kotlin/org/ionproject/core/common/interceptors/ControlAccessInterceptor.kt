package org.ionproject.core.common.interceptors

import org.ionproject.core.accessControl.AccessControlCache
import org.ionproject.core.accessControl.CaffeineConfiguration
import org.ionproject.core.accessControl.PDP
import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.accessControl.pap.entities.DerivedTokenClaims
import org.ionproject.core.accessControl.pap.entities.TokenClaims
import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

data class Request(val method: String, val path: String, val resourceIdentifier: ResourceIdentifierDescriptor)
data class ResourceIdentifierDescriptor(val resource: String, val version: String)

/**
 * Policy Enforcement Point
 * It will enforce the decision of the PDP
 */
class ControlAccessInterceptor : HandlerInterceptorAdapter() {

    private val includeType = "bearer"
    private val authenticationQueryParameter = "access_token"
    private val authenticationHeaderAuthorization = "Authorization"

    private val tokenGenerator: TokenGenerator = TokenGenerator()
    private val pdp: PDP = PDP()
    private val cache: AccessControlCache = AccessControlCache()

    /**
     * Intercepts the request, tries to identify the authentication mode...
     * returns true if access was granted
     * returns false if access was not granted
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        //Obtain name of resource trying to access (the name of the resource is the one in the annotation
        val resourceIdentifier = buildResourceIdentifier(handler)
        val requestDescriptor = Request(request.method, request.requestURI, resourceIdentifier)

        //Checks if its an import link verification path
        if (checkAccessToken(request, requestDescriptor))
            return true

        //Authorization Header verification path
        checkAuthorizationHeader(request, requestDescriptor)
        return true
    }

    /**
     * During an authorization header authentication mode
     * checks if the header is present and if the format is valid to proceed with further
     * checks.
     */
    private fun checkAuthorizationHeader(request: HttpServletRequest, requestDescriptor: Request) {
        val header = request.getHeader(authenticationHeaderAuthorization)

        //Checks to see if request has authorization header
        if (header == null) {
            logger.info(
                LogMessages.forAuthError(
                    LogMessages.tokenHeaderAuth,
                    LogMessages.noToken
                )
            )

            throw UnauthenticatedUserException(LogMessages.noTokenException)
        }

        val pair = header.trim().split(" ")

        //Client includes header "Authorization" with bad value e.g. "Bearer      "
        if (pair.size != 2) {
            logger.info(
                LogMessages.forAuthError(
                    LogMessages.tokenHeaderAuth,
                    LogMessages.invalidFormatHeader
                )
            )

            throw BadRequestException(LogMessages.incorrectAuthHeaderFormatException)
        }

        //Client include token type is different than "Bearer"
        val tokenIncludeType = pair[0].toLowerCase()
        if (tokenIncludeType != includeType) {

            logger.info(
                LogMessages.forAuthError(
                    LogMessages.tokenHeaderAuth,
                    LogMessages.unsupportedIncludeType
                )
            )

            throw BadRequestException(LogMessages.unsupportedIncludeTypeException)
        }

        val reference = pair[1]

        //Check remaining policies
        val tokenHash = getTokenHash(
            reference,
            LogMessages.tokenHeaderAuth
        )

        val token = cache.getToken(tokenHash, false)
        val claims = token.claims as TokenClaims

        pdp.evaluateAuthorization(token, requestDescriptor, claims.scope, LogMessages.tokenHeaderAuth)
        request.setAttribute("token", token)
    }

    /**
     * Returns true if it was an import url authentication mode and succeeded
     * Returns false if it wasn't an import url authentication
     */
    private fun checkAccessToken(request: HttpServletRequest, requestDescriptor: Request): Boolean {
        val reference = request.getParameter(authenticationQueryParameter)
        if (reference != null) {

            val tokenHash = getTokenHash(
                reference,
                LogMessages.importUrlAuth
            )

            val token = cache.getToken(tokenHash, true)
            val claims = token.claims as DerivedTokenClaims

            pdp.evaluateAuthorization(token, requestDescriptor, claims.scope, LogMessages.importUrlAuth)
            return true
        }

        return false
    }

    /**
     * Builds an object that describes the controller trying to gain access (id, version)
     * used to check policies
     */
    private fun buildResourceIdentifier(handler: Any): ResourceIdentifierDescriptor {
        val handlerMethod = handler as HandlerMethod
        val resourceIdentifierAnnotation = handlerMethod.getMethodAnnotation(ResourceIdentifierAnnotation::class.java)
        val resourceName = resourceIdentifierAnnotation?.resourceName ?: ""
        val resourceVersion = resourceIdentifierAnnotation?.version ?: ""
        return ResourceIdentifierDescriptor(resourceName, resourceVersion)
    }

    /**
     * Builds the hash out of the reference
     */
    private fun getTokenHash(
        tokenReference: String,
        authMode: String
    ): String {
        try {
            val tokenBase64Decoded = tokenGenerator.decodeBase64url(tokenReference)
            return tokenGenerator.getHash(tokenBase64Decoded)

        } catch (e: Exception) {
            logger.info(
                LogMessages.forAuthError(
                    authMode,
                    LogMessages.tokenHashError
                )
            )

            throw UnauthenticatedUserException(LogMessages.incorrectTokenFormat)
        }
    }

}
