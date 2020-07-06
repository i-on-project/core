package org.ionproject.core.common.interceptors

import org.ionproject.core.accessControl.PDP
import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)
private const val includeType = "bearer"

data class Request(val method: String, val apiVersion: String, val resource: String)

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
        request.setAttribute("serverName", request.scheme + "://" + request.serverName)

        //If the request is one made by an import link's it will instead of a Authorization header
        //contain the query parameter access_token
        /**
         * If the user includes an access_token query parameter when he has a valid token to read the resource
         * in the header Authorization, this current way will ignore the Authorization header, is this a correct
         * behavior?
         */
        val jwtToken = request.getParameter("access_token")
        if(jwtToken != null) {
            pdp.evaluateJwtToken(jwtToken, request.method, request.requestURI)
            logger.info("An access_token query parameter authentication method succeeded for location \"${request.servletPath}\".")
            return true
        }

        //Client doesn't include header "Authorization"
        val header = request.getHeader("Authorization")
            ?: throw UnauthenticatedUserException("User not authenticated.")
        val pair = header.trim().split(" ")

        //Client includes header "Authorization" with bad value e.g. "Bearer      "
        if (pair.size != 2)
            throw BadRequestException("Incorrect authorization header value.")

        //Client include token type is different than "Bearer"
        val tokenIncludeType = pair[0].toLowerCase()
        if (tokenIncludeType != includeType)
            throw BadRequestException("Unsupported include token type.")

        //Transforms the base64url encoded value to the SHA-256 hashed value
        val tokenBase64Decoded = tokenGenerator.decodeBase64url(pair[1])
        val tokenHash = tokenGenerator.getHash(tokenBase64Decoded)

        //Sends the request with the token Hash down to the Policy Decision Point
        val requestDescriptor: Request = buildRequestDescriptor(request.requestURI, request.method)

        //Checks if the token is valid, if any policy is not valid an exception will be thrown
        //and the next interceptor won't be called
        val clientId = pdp.evaluateRequest(tokenHash, requestDescriptor)

        request.setAttribute("clientId", clientId)
        return true
    }

    private fun buildRequestDescriptor(pathInfo: String, method: String): Request {
        val requestDescriptor: Request
        val parts = pathInfo.substring(1).split("/")

        if (parts.size == 1) //Special case user is accessing endpoint without version
            requestDescriptor = Request(method, "*", pathInfo)
        else {
            val idxVersion = pathInfo.indexOf("/", 1)
            requestDescriptor = Request(method, pathInfo.substring(1, idxVersion), pathInfo.substring(idxVersion))
        }

        return requestDescriptor
    }


}