package org.ionproject.core.readApi.common.interceptors

import org.ionproject.core.accessControl.PDP
import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.readApi.common.customExceptions.BadRequestException
import org.ionproject.core.readApi.common.customExceptions.UnauthenticatedUserException
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
    private val tokenGenerator : TokenGenerator = TokenGenerator()

    @Resource
    private val pdp: PDP = PDP()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //Client doesn't include header "Authorization"
        val header = request.getHeader("Authorization")
                ?: throw UnauthenticatedUserException("User not authenticated.")
        val pair = header.trim().split(" ")

        //Client includes header "Authorization" with bad value e.g. "Bearer      "
        if(pair.size != 2)
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
        if (pdp.evaluateRequest(tokenHash, requestDescriptor))
            return true

        return false
    }

    private fun buildRequestDescriptor(pathInfo: String, method: String): Request {
        val requestDescriptor: Request
        val parts = pathInfo.substring(1).split("/")

        if(parts.size == 1) //Special case user is accessing endpoint without version
            requestDescriptor = Request(method, "*", pathInfo)
        else {
            val idxVersion = pathInfo.indexOf("/", 1)
            requestDescriptor = Request(method, pathInfo.substring(1, idxVersion), pathInfo.substring(idxVersion))
        }

        return requestDescriptor
    }


}