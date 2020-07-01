package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.representations.ImportLinkRepr
import org.ionproject.core.accessControl.representations.TokenIssueDetails
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.common.customExceptions.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
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
    @PostMapping(Uri.issueToken, consumes = [Media.APPLICATION_JSON])
    fun issueToken(@RequestBody tokenIssueDetails: TokenIssueDetails): ResponseEntity<TokenRepr> {
        val token: TokenRepr = services.generateToken(tokenIssueDetails.scope)
        return ResponseEntity.ok(token)
    }

    /**
     * According to [RFC 7009] , the body should be of content-type "application/x-wwww-form-urlencoded" and contain the token to revoke
     * Invalid tokens revoke requests should also return 200 OK
     *
     * The only occasion the response of this handler is not 200 OK is when the body has not used the key token,
     * which is a behavior not specified by the RFC.
     *
     * When client secrets are added a new policy should be checked, if the token was issued by the client,
     * if that validation fails the client should be informed.
     *
     */
    @PostMapping(Uri.revokeToken, consumes = [Media.FORM_URLENCODED_VALUE])
    fun revokeToken(@RequestParam body: Map<String, String>): ResponseEntity<Any> {
        val token = body["token"]
        if (token.isNullOrEmpty())
            throw BadRequestException("No token specified.")

        services.revokeToken(token)
        return ResponseEntity.ok().build()
    }


    /**
     * Generates an import link for a class Calendar
     */
    @GetMapping(Uri.importClassCalendar)
    fun importClassCalendar(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @RequestParam query: Map<String, String>,
        @RequestAttribute("clientId") clientId: Int
    ): ResponseEntity<Any> {

        var url = Uri.baseUrl + Uri.forCalendarByClass(cid, calterm).toString()
        url = addQueryParams(url, query)

        val jwt = services.generateImportToken(url, clientId)
        url += jwt

        return ResponseEntity.ok().body(ImportLinkRepr(url))
    }

    /**
     * Generates an import link for a class section Calendar
     */
    @GetMapping(Uri.importClassSectionCalendar)
    fun importClassSectionCalendar(
        @PathVariable sid: String,
        @PathVariable calterm: String,
        @PathVariable cid: Int,
        @RequestParam query: Map<String, String>,
        @RequestAttribute("clientId") clientId: Int
    ): ResponseEntity<Any> {

        var url = Uri.baseUrl + Uri.forCalendarByClassSection(cid, calterm, sid).toString()
        url = addQueryParams(url, query)

        val jwt = services.generateImportToken(url, clientId)
        url += jwt

        return ResponseEntity.ok().body(ImportLinkRepr(url))
    }

    /**
     * Concatenates the query parameters to the url
     */
    private fun addQueryParams(url: String, query: Map<String, String>) : String {
        var url = "$url?"
        for (key in query.keys) {
            url += "$key=${query[key]}&"
        }

        return url.dropLast(1) // removes the last &
    }

}
