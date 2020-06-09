package org.ionproject.core.common.interceptors

import org.ionproject.core.access_control.PDP
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.security.MessageDigest
import java.util.*
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
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val header = request.getHeader("Authorization")
                ?: throw UnauthenticatedUserException("User not authenticated.")
        val pair = header.trim().split(" ")

        val tokenIncludeType = pair[0].toLowerCase()
        if (tokenIncludeType != includeType)
            throw BadRequestException("Unsupported include token type.")

        val tokenRef = decodeBase64(pair[1])
        val tokenIndex = getHash(tokenRef)

        val requestDescriptor: Request = buildRequestDescriptor(request.requestURI, request.method)
        if (PDP.evaluateRequest(tokenIndex, requestDescriptor))
            return true
        else
            throw ForbiddenActionException("You Require higher privileges to do that.")
    }

    private fun buildRequestDescriptor(pathInfo: String, method: String): Request {
        val requestDescriptor: Request

        /**
         * Unexistent URI's e.g. "/foo/bar" without a controller associated are redirected
         * to the endpoint "/error" by the spring framework, which will then hit the
         * authentication interceptor.
         * Should this request be terminated and avoid wasting resources like below or allow to be further processed?
         */
        if (pathInfo == "/error")
            throw ResourceNotFoundException("That resource is unexistent...")

        if (pathInfo == "/")     //Special case user is accessing the HOME document and there is no API version
            requestDescriptor = Request(method, "*", "/")
        else {
            val idxVersion = pathInfo.indexOf("/", 1)
            requestDescriptor = Request(method, pathInfo.substring(1, idxVersion), pathInfo.substring(idxVersion))
        }

        return requestDescriptor
    }

    private fun decodeBase64(tokenBase64: String): String {
        val decoder = Base64.getDecoder().decode(tokenBase64)
        return String(decoder)
    }

    private fun getHash(tokenRef: String): String {
        val bytes = tokenRef.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        //Print bytes in hexadecimal format with padding in case of insufficient chars (used to index the token table)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}