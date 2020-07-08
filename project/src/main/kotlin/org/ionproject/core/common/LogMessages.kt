package org.ionproject.core.common

object LogMessages {

    fun forErrorImport(method: String, url: String, reason: String) =
        "TYPE:[IMPORT URL] |  METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS DENIED] | REASON:[$reason]"

    fun forErrorImportDetail(derivedTokenReference: String, fatherTokenHash: String, method: String, requestUrl: String, reason: String) =
        "TYPE:[IMPORT URL] | ACCESS_TOKEN:[${derivedTokenReference}] " +
        "| ISSUED_BY:[${fatherTokenHash}] | METHOD:[${method}] " +
        "| LOCATION:[\"${requestUrl}\"] | RESULT:[ACCESS DENIED] " +
        "| REASON:[$reason]"

    fun forSuccessImport(derivedTokenReference: String, fatherTokenHash: String, method: String, requestUrl: String) =
        "TYPE:[IMPORT URL] | ACCESS_TOKEN:[${derivedTokenReference}] " +
        "| ISSUED_BY:[${fatherTokenHash}] | METHOD:[${method}] " +
        "| LOCATION:[\"${requestUrl}\"] | RESULT:[ACCESS GRANTED]"

    fun forError(method: String, url: String, reason: String) =
        "TYPE:[AUTHORIZATION HEADER] |  METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS DENIED] | REASON:[$reason]"

    fun forErrorDetail(tokenHash: String, method: String, requestUrl: String, reason: String) =
        "TYPE:[AUTHORIZATION HEADER] | TOKEN_HASH:[${tokenHash}] " +
        "| METHOD:[${method}] | LOCATION:[${requestUrl}] | RESULT:[ACCESS DENIED] " +
        "| REASON:[$reason]"

    fun forSuccess(method: String, url: String) =
        "TYPE:[AUTHORIZATION HEADER] |  METHOD:[${method}] | LOCATION:[${url}] | RESULT:[ACCESS GRANTED]"
}