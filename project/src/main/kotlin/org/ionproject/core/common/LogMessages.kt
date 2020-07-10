package org.ionproject.core.common

object LogMessages {
    var logMessageId : Long = 0

    //Authentication modes
    const val importUrlAuth = "IMPORT URL"
    const val tokenHeaderAuth = "AUTHORIZATION HEADER"

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
    const val noTokenException = "User not authenticated, no token on Authorization Header"
    const val incorrectAuthHeaderFormatException = "Incorrect authorization header format value."
    const val unsupportedIncludeTypeException = "Unsupported include token type, it must be Bearer."
    const val inexistentTokenMessage = "The token you have presented can't be found or doesn't exist, try requesting a new one."
    const val incorrectTokenFormat = "The token you have presented seems to be incorrect, check the value."
    const val tokenExpiredMessage = "The token you have presented is expired, try requesting a new one."
    const val tokenRevokedMessage = "The token you have presented is revoked, try requesting a new one."
    const val lackOfPrivilegesMessage = "The token you possess doesn't give you permissions for that action."
    const val unsafeMethodMessage = "This type of access doesn't allow unsafe methods (PUT, POST...)."

    //Exception Errors
    const val internalServerError = "Internal Server Error, something on our side happened, try again later. If the problem persists contact the development team."
    const val unsupportedMediaType = "The API doesn't support to the current location the media type you specified. Check the documentation"
    const val notFoundResource = "The resource your tried to access can't be found on our servers."
    const val unknownError = "It seems the error you received isn't yet documented, inform the developers."

    //Authentication Messages
    fun forAuthError(authenticationMode: String, method: String, url: String, reason: String) =
        "[ID:${logMessageId++}] [AUTHENTICATION] | TYPE:[$authenticationMode] |  METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS DENIED] | REASON:[$reason]"

    fun forAuthErrorDetail(authenticationMode: String, tokenHash: String, method: String, requestUrl: String, reason: String) =
        "[ID:${logMessageId++}] [AUTHENTICATION] | TYPE:[$authenticationMode] | TOKEN_HASH:[${tokenHash}] " +
        "| METHOD:[${method}] | LOCATION:[${requestUrl}] | RESULT:[ACCESS DENIED] " +
        "| REASON:[$reason]"

    fun forAuthSuccess(authenticationMode: String, tokenHash: String, method: String, url: String) =
        "[ID:${logMessageId++}] [AUTHENTICATION] | TYPE:[$authenticationMode] | TOKEN:[${tokenHash}] | METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS GRANTED]"

    //Exception Messages
    fun forException(url: String, detail: String) =
        "[ID:${logMessageId++}] [EXCEPTION] | LOCATION:[$url] | DETAIL:[$detail]"

    //Logger interceptor message
    fun forLoggerAccessMessage(ip: String, method: String, url: String, startTime: Long) =
        "[ID:${logMessageId++}] [INFORMATION] | IP:[${ip}] | Method:[${method}] | Endpoint:[${url}] | Timestamp:[${startTime}]"

    fun forLoggerCompletionMessage(startTime: Long, endTime: Long) =
        "[ID:${logMessageId++}] [INFORMATION] | Total time taken to process request in milliseconds: ${endTime - startTime} ms"
}