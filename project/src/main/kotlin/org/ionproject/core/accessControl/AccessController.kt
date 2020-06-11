package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.representations.TokenIssueDetails
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.accessControl.representations.TokenRevokedRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

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
    @PutMapping(Uri.issueToken, consumes=[Media.APPLICATION_JSON])
    fun issueToken(@RequestBody tokenIssueDetails: TokenIssueDetails): ResponseEntity<TokenRepr> {
        val token: TokenRepr = services.generateToken(tokenIssueDetails.scope)
        return ResponseEntity.ok(token)
    }

    /**
     * It receives the token that must be revoked in the Authorization Header
     *
     * All scopes are allowed to access this endpoint.
     * If the token is valid the operation will always succeed.
     */
    @PutMapping(Uri.revokeToken)
    fun revokeToken(@RequestHeader("Authorization") token : String): ResponseEntity<TokenRevokedRepr> {
        services.revokeToken(token)
        return ResponseEntity.ok(TokenRevokedRepr("Token revoked."))
    }
}
