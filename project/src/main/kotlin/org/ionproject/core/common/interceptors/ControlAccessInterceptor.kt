package org.ionproject.core.common.interceptors

import org.ionproject.core.accessControl.PDP
import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.accessControl.pap.entities.DerivedTokenClaims
import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)
private const val includeType = "bearer"

data class Request(val method: String, val path: String, val resourceIdentifier: ResourceIdentifierDescriptor)
data class ResourceIdentifierDescriptor(val resource: String, val version: String)

/**
 * Policy Enforcement Point
 * It will enforce the decision of the PDP
 */
class ControlAccessInterceptor : HandlerInterceptorAdapter() {

    @Resource
    private val tokenGenerator: TokenGenerator = TokenGenerator()

    @Resource
    private val pdp: PDP = PDP()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        //Checks if its an import link verification path
        val accessToken = request.getParameter("access_token")
        if(accessToken != null) {
            val token = pdp.evaluateAccessToken(accessToken, request.method, request.requestURI)
            val claims = token.claims as DerivedTokenClaims

            logger.info(LogMessages.forSuccessImport(
                claims.derivedTokenReference,
                claims.fatherTokenHash,
                request.method,
                request.requestURI)
            )
            return true
        }

        //Client doesn't include header "Authorization"
        val header = request.getHeader("Authorization")

            if(header == null) {
                logger.info(LogMessages.forError(request.method, request.requestURI, "NO TOKEN PRESENTED"))
                throw UnauthenticatedUserException("User not authenticated, no token on Authorization Header")
            }
        val pair = header.trim().split(" ")

        //Client includes header "Authorization" with bad value e.g. "Bearer      "
        if (pair.size != 2) {
            logger.info(LogMessages.forError(request.method, request.requestURI,
                "INCORRECT AUTHORIZATION HEADER FORMAT VALUE"))
            throw BadRequestException("Incorrect authorization header format value.")
        }

        //Client include token type is different than "Bearer"
        val tokenIncludeType = pair[0].toLowerCase()
        if (tokenIncludeType != includeType) {
            logger.info(LogMessages.forError(request.method, request.requestURI,
                "UNSUPPORTED TOKEN INCLUDE TYPE"))
            throw BadRequestException("Unsupported include token type, it must be Bearer.")
        }

        //Transforms the base64url encoded value to the SHA-256 hashed value
        val tokenBase64Decoded = tokenGenerator.decodeBase64url(pair[1])
        val tokenHash = tokenGenerator.getHash(tokenBase64Decoded)

        //Obtain name of resource trying to access (the name of the resource is the one in the annotation
        val resourceIdentifier = buildResourceIdentifier(handler)
        val requestDescriptor = Request(request.method, request.requestURI, resourceIdentifier)

        //Verify the policies, if any fails the next interceptor won't be called and the processing will end
        val token = pdp.evaluateRequest(tokenHash, requestDescriptor)

        //Optimization to avoid querying the db again for this same information
        request.setAttribute("token", token)
        return true
    }

    private fun buildResourceIdentifier(handler: Any) : ResourceIdentifierDescriptor {
        val handlerMethod = handler as HandlerMethod
        val resourceIdentifierAnnotation = handlerMethod.getMethodAnnotation(ResourceIdentifierAnnotation::class.java)
        val resourceName = resourceIdentifierAnnotation?.resourceName ?: ""
        val resourceVersion = resourceIdentifierAnnotation?.version ?: ""
        return ResourceIdentifierDescriptor(resourceName, resourceVersion)
    }

}
