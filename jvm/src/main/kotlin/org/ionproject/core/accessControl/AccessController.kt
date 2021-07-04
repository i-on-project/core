package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.TokenClaims
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.representations.ImportLinkRepr
import org.ionproject.core.accessControl.representations.TokenIssueDetails
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Uri
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AccessController(private val services: AccessServices, private val tokenGenerator: TokenGenerator) {

    private val privilegedRevokeScope = "urn:org:ionproject:scopes:api:revoke"

    /**
     * Generates token based on the scope requested and saves it to the database.
     *
     * The correct issue token must be sent along with the request to validate
     * the issue request.
     *
     * This endpoint is accessible only to the client who presents the
     * token with the "urn:org:ionproject:scopes:token:issue" scope.
     */
    @ResourceIdentifierAnnotation(ResourceIds.ISSUE_TOKEN, ResourceIds.ALL_VERSIONS)
    @PostMapping(Uri.issueToken, consumes = [Media.APPLICATION_JSON])
    fun issueToken(@RequestBody tokenIssueDetails: TokenIssueDetails): ResponseEntity<TokenRepr> {
        val token: TokenRepr = services.generateToken(tokenIssueDetails.scope)
        return ResponseEntity.ok(token)
    }

    /**
     * According to [RFC 7009] , the body should be of content-type "application/x-wwww-form-urlencoded" a
     * nd contain the token to revoke
     *
     * Invalid tokens revoke requests should also return 200 OK, but this condition can't be fully respected as there is
     * the chance that a token which has permission to access revoke endpoint (write) would revoke a read (PUBLIC) token.
     *
     * When client secrets are added a new policy should be checked, if the token was issued by the client,
     * if that validation fails the client should be informed.
     *
     * Read Tokens and they're derived can only be revoked with a special token that must possess the scope
     * stored in the constant "privilegedRevokeScope" (this token is only known by core members)
     */
    @ResourceIdentifierAnnotation(ResourceIds.REVOKE_TOKEN, ResourceIds.ALL_VERSIONS)
    @PostMapping(Uri.revokeToken, consumes = [Media.FORM_URLENCODED_VALUE])
    fun revokeToken(
        @RequestParam body: Map<String, String>,
        @RequestAttribute("token") token: TokenEntity
    ): ResponseEntity<Any> {
        val tokenBody = body["token"]
        if (tokenBody.isNullOrEmpty())
            throw BadRequestException("No token specified.")

        val tokenBodyHash = tokenGenerator.getHash(tokenGenerator.decodeBase64url(tokenBody))

        // Check for special revoke token
        val claims = token.claims as TokenClaims
        if (claims.scope == privilegedRevokeScope) {
            // Privileged path

            when (body["operation"]) {
                "1" -> { // revokeChild
                    services.revokeChild(tokenBodyHash)
                }
                "2" -> { // revokePresented
                    services.revokeToken(tokenBodyHash)
                }
                "3" -> { // revokePresentedAndChild
                    services.revokePresentedAndChild(tokenBodyHash)
                }
                else -> { // DEFAULT revokePresented
                    services.revokeToken(tokenBodyHash)
                }
            }
        } else {
            // Unprivileged path, can only revoke the presented token

            if (token.hash != tokenBodyHash)
                throw ForbiddenActionException("You can't revoke another token besides the presented one.")

            services.revokeToken(tokenBodyHash)
        }

        return ResponseEntity.ok().build()
    }

    /**
     * Generates an import link for a class Calendar
     */
    @ResourceIdentifierAnnotation(ResourceIds.IMPORT_CLASS_CALENDAR, ResourceIds.VERSION_0)
    @GetMapping(Uri.importClassCalendar)
    fun importClassCalendar(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @RequestParam query: MultiValueMap<String, String>,
        @RequestAttribute("token") tokenFather: TokenEntity
    ): ResponseEntity<Any> {

        var parameterPath = Uri.forCalendarByClass(cid, calterm).toString()

        val token = services.generateImportClassCalendar(cid, calterm, query, tokenFather.hash)

        val url = buildUrl(query, parameterPath, token)
        return ResponseEntity.ok().body(ImportLinkRepr(url))
    }

    /**
     * Generates an import link for a class section Calendar
     */
    @ResourceIdentifierAnnotation(ResourceIds.IMPORT_CLASS_SECTION_CALENDAR, ResourceIds.VERSION_0)
    @GetMapping(Uri.importClassSectionCalendar)
    fun importClassSectionCalendar(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @RequestParam query: MultiValueMap<String, String>,
        @RequestAttribute("token") tokenFather: TokenEntity
    ): ResponseEntity<Any> {
        val parameterPath = Uri.forCalendarByClassSection(cid, calterm, sid).toString()

        val token = services.generateImportClassSectionCalendar(sid, calterm, cid, query, tokenFather.hash)

        val url = buildUrl(query, parameterPath, token)
        return ResponseEntity.ok().body(ImportLinkRepr(url))
    }

    private fun buildUrl(query: MultiValueMap<String, String>, parameterPath: String, derivedToken: String): String {
        var queryParams = "?"
        queryParams += if (query.size == 0)
            derivedToken
        else
            addQueryParams(query) + "&$derivedToken"

        return parameterPath + queryParams
    }

    /**
     * Concatenates the query parameters to the url
     */
    private fun addQueryParams(
        query: MultiValueMap<String, String>
    ): String {
        var queryString = ""
        var listParams: String
        for (key in query.keys) {
            val list = query[key]

            listParams = if (list == null || list.size == 0)
                continue
            else if (list.size == 1)
                list[0]
            else
                query[key]?.fold(query[key]?.get(0)) { str, it -> "$str,$it" } ?: ""

            queryString += "$key=$listParams&"
        }

        return queryString.dropLast(1) // removes the last &
    }
}
