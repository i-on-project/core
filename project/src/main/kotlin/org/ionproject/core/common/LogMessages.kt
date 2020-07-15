package org.ionproject.core.common

import org.ionproject.core.common.filters.REQUEST_ID
import org.slf4j.MDC
import java.util.concurrent.atomic.AtomicLong

object LogMessages {
    private var logMessageId : AtomicLong = AtomicLong(0)

    //Event type
    const val httpStartEvent = "http_in_start"
    const val httpEndEvent = "http_in_end"
    const val exceptionEvent = "exception_in_processing"
    const val successAuthEvent = "success_auth"
    const val unsuccessAuthEvent = "unsuccessful_auth"

    //Authentication Errors & Exceptions
    const val lackPrivileges = "Lack of privileges."
    const val invalidMethod = "Invalid method."
    const val inexistentToken = "Token Not Found."
    const val revokedToken = "Revoked Token."
    const val tokenExpired = "Token Expired."
    const val noToken = "No token presented."
    const val invalidFormatHeader = "Authorization header token format is invalid."
    const val unsupportedIncludeType = "Unsupported include type."
    const val tokenHashError = "Error obtaining hash out of presented token reference."

    //This messages are meant for the user, therefore they must have more information than the error messages that we log
    const val noTokenException = "User not authenticated, no token presented in the Authorization Header"
    const val incorrectAuthHeaderFormatException = "Incorrect authorization header format value."
    const val unsupportedIncludeTypeException = "Unsupported include token type, it must be Bearer."
    const val inexistentTokenMessage = "The token you have presented can't be found or doesn't exist, try requesting a new one."
    const val incorrectTokenFormat = "The token you have presented seems to be incorrect, check the value."
    const val tokenExpiredMessage = "The token you have presented is expired, try requesting a new one."
    const val tokenRevokedMessage = "The token you have presented is revoked, try requesting a new one."
    const val lackOfPrivilegesMessage = "The token you possess doesn't give you permissions for that action."

    //Exception Errors
    const val internalServerError = "Internal Server Error, something on our side happened, try again later. If the problem persists contact the development team."
    const val unsupportedMediaType = "The API doesn't support to the current location the media type you specified. Check the documentation"
    const val notFoundResource = "The resource your tried to access can't be found on our servers."
    const val unknownError = "It seems the error you received isn't yet documented, inform the developers."

    //Authentication Messages
    fun forAuthError(reason: String) =
        "$unsuccessAuthEvent [rid:${MDC.get(REQUEST_ID)} | uid:${logMessageId.getAndIncrement()}] | REASON:[$reason]"

    fun forAuthErrorDetail(tokenHash: String, reason: String) =
        "$unsuccessAuthEvent [rid:${MDC.get(REQUEST_ID)} | uid:${logMessageId.getAndIncrement()}] | TOKEN_HASH:[${tokenHash}] " +
        "| REASON:[$reason]"

    fun forAuthSuccess(tokenHash: String) =
        "$successAuthEvent [rid:${MDC.get(REQUEST_ID)} | uid:${logMessageId.getAndIncrement()}] | TOKEN:[${tokenHash}]"

    //Exception Messages
    fun forException(url: String, detail: String) =
        "$exceptionEvent [rid:${MDC.get(REQUEST_ID)} | uid:${logMessageId.getAndIncrement()}] | URI:[$url] | DETAIL:[$detail]"

    //Logger interceptor message
    fun forLoggerAccessMessage(ip: String, method: String, url: String, template: String) =
        "$httpStartEvent [rid:${MDC.get(REQUEST_ID)} | uid:${logMessageId.getAndIncrement()}] | IP:[${ip}] | Method:[${method}] | URI:[${url}] | Template:[${template}]"

    fun forLoggerCompletionMessage(total: Long) =
        "$httpEndEvent [rid:${MDC.get(REQUEST_ID)} | uid:${logMessageId.getAndIncrement()}] | Total time taken to process request in milliseconds: ${total} ms"
}