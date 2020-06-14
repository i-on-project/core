package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.representations.TokenIssueDetails
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.accessControl.representations.TokenRevokedRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AccessController(private val services: AccessServices) {
    /**
     * Generates token based on the scope requested and saves it to the database.
     *
     * The correct issue token must be sent along with the request to validate
     * the issue request.
     *
     * This endpoint is accessible only to the client who presents the
     * token with the "urn:org:ionproject:scopes:token:issue" scope.
     */
    @PostMapping(Uri.issueToken, consumes=[Media.APPLICATION_JSON])
    fun issueToken(@RequestBody tokenIssueDetails: TokenIssueDetails): ResponseEntity<TokenRepr> {
        val token: TokenRepr = services.generateToken(tokenIssueDetails.scope)
        return ResponseEntity.ok(token)
    }

    /**
     * It receives the token that must be revoked in the Authorization Header
     *
     * All scopes are allowed to access this endpoint.
     * If the token is valid the operation will always succeed.
     *
     * According to [RFC 7009] , the body should be of content-type "application/x-wwww-form-urlencoded" and
     * contain the token to revoke
     * but in this beta access manager the token and client secret are the same, there is no point
     * in adding extra info at the moment.
     */
    @PostMapping(Uri.revokeToken)
    fun revokeToken(@RequestHeader("Authorization") token : String) : ResponseEntity<Any> {
        services.revokeToken(token)
        return ResponseEntity.ok().build()
    }
}
