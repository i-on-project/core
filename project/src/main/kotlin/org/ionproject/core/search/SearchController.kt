package org.ionproject.core.search

import org.ionproject.core.common.ProblemJson
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Uri
import org.ionproject.core.search.model.InvalidSearchQuerySyntaxException
import org.ionproject.core.search.model.InvalidSearchTypeException
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.representations.toSearchResultListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController(
    private val searchRepo: SearchRepo
) {

    @ExceptionHandler(value = [InvalidSearchTypeException::class])
    fun handleInvalidSearchType(
        ex: InvalidSearchTypeException,
        request: HttpServletRequest
    ) = handleExceptionResponse(
        "https://github.com/i-on-project/core/docs/api/search.md#invalid-type",
        "Invalid type defined in [types] query parameter.",
        400,
        "The not supported type \"${ex.type}\" was used.",
        "https://github.com/i-on-project/core/docs/api/search.md#invalid-type"
    )

    @ExceptionHandler(value = [InvalidSearchQuerySyntaxException::class])
    fun handleInvalidSearchQuerySyntax(
        ex: InvalidSearchQuerySyntaxException,
        request: HttpServletRequest
    ) = handleExceptionResponse(
        "https://github.com/i-on-project/core/docs/api/search.md#invalid-query",
        "Invalid search syntax used in [query] query parameter.",
        400,
        "The \"${ex.query}\" query has invalid syntax.",
        "https://github.com/i-on-project/core/docs/api/search.md#invalid-query"
    )

    @ResourceIdentifierAnnotation(ResourceIds.SEARCH, ResourceIds.VERSION_0)
    @GetMapping(Uri.search)
    fun search(
        @RequestParam("query") query: String,
        @RequestParam("types", required = false, defaultValue = SearchableEntities.ALL) types: List<String>,
        pagination: Pagination
    ): ResponseEntity<Any> {
        val searchQuery = SearchQuery(query, types, pagination.limit, pagination.page)
        val searchResults = searchRepo.search(searchQuery)
        return ResponseEntity.ok(searchResults.toSearchResultListRepr())
    }
}
