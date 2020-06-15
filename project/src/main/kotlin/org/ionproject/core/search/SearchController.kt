package org.ionproject.core.search

import org.ionproject.core.common.ProblemJson
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

    @GetMapping(Uri.search)
    fun search(
        @RequestParam("query") query: String,
        @RequestParam("types", required = false, defaultValue = SearchableEntities.ALL) types: List<String>,
        @RequestParam("limit", required = false, defaultValue = "10") limit: Int,
        @RequestParam("page", required = false, defaultValue = "0") page: Int
    ): ResponseEntity<Any> {
        try {
            val searchQuery = SearchQuery(query, types, limit, page)

            val searchResults = searchRepo.search(searchQuery)

            return ResponseEntity.ok(searchResults.toSearchResultListRepr())
        } catch (e: InvalidSearchTypeException) {
            return ResponseEntity.badRequest().body(
                ProblemJson(
                    "https://github.com/i-on-project/core/docs/api/search.md#invalid-type",
                    "Invalid type defined in [types] query parameter.",
                    400,
                    "The not supported type \"${e.type}\" was used.",
                    "https://github.com/i-on-project/core/docs/api/search.md#invalid-type"
                )
            )
        } catch (e: InvalidSearchQuerySyntaxException) {
            return ResponseEntity.badRequest().body(
                ProblemJson(
                    "https://github.com/i-on-project/core/docs/api/search.md#invalid-query",
                    "Invalid search syntax used in [query] query parameter.",
                    400,
                    "The \"${e.query}\" query has invalid syntax.",
                    "https://github.com/i-on-project/core/docs/api/search.md#invalid-query"
                )
            )
        }

    }
}
