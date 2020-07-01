package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.representations.TokenIssueDetails
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.common.customExceptions.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
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
    @PostMapping(Uri.revokeToken, consumes =[Media.FORM_URLENCODED_VALUE])
    fun revokeToken(@RequestParam body: Map<String,String>) : ResponseEntity<Any> {
        val token = body["token"]
        if(token.isNullOrEmpty())
            throw BadRequestException("No token specified.")

        services.revokeToken(token)
        return ResponseEntity.ok().build()
    }


    @GetMapping(Uri.importClassCalendar)
    fun importClassCalendar(@PathVariable cid: Int,
                            @PathVariable calterm: String,
                            @RequestParam query: MultiValueMap<String, String>): ResponseEntity<Any> {

        var url = Uri.forCalendarByClass(cid, calterm).toString()
        url = addQueryParams(url, query)
    }

    @GetMapping(Uri.importClassSectionCalendar)
    fun importClassSectionCalendar(@PathVariable sid: String,
                                   @PathVariable calterm: String,
                                   @PathVariable cid: Int,
                                   @RequestParam query: MultiValueMap<String, String>): ResponseEntity<Any> {

    }

    private fun addQueryParams(url: String, query: MultiValueMap<String, String>) : String {
        var url = "$url?"
        for (key in query) {
            url += "$key=${query[key]}"
        }
    }

}
