package org.ionproject.core.common

object LogMessages {

    //Authentication modes
    const val importUrlAuth = "IMPORT URL"
    const val tokenHeaderAuth = "AUTHORIZATION HEADER"

    //Errors
    const val lackPrivileges = "Lack of privileges."
    const val invalidMethod = "Invalid method."
    const val inexistentToken = "Token Not Found."
    const val revokedToken = "Revoked Token."
    const val tokenExpired = "Token Expired."
    const val noToken = "No token presented."
    const val invalidFormatHeader = "Authorization header token format is invalid."
    const val unsupportedIncludeType = "Unsupported include type."

    //Messages
    fun forError(authenticationMode: String, method: String, url: String, reason: String) =
        "TYPE:[$authenticationMode] |  METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS DENIED] | REASON:[$reason]"

    fun forErrorDetail(authenticationMode: String, tokenHash: String, method: String, requestUrl: String, reason: String) =
        "TYPE:[$authenticationMode] | TOKEN_HASH:[${tokenHash}] " +
        "| METHOD:[${method}] | LOCATION:[${requestUrl}] | RESULT:[ACCESS DENIED] " +
        "| REASON:[$reason]"

    fun forSuccess(authenticationMode: String, tokenHash: String, method: String, url: String) =
        "TYPE:[$authenticationMode] | TOKEN:[${tokenHash}] | METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS GRANTED]"
}