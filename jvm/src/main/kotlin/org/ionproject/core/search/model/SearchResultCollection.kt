package org.ionproject.core.search.model

class SearchResultCollection(
    val resultOf: SearchQuery,
    val results: List<SearchResult>
) : Iterable<SearchResult> {

    override fun iterator(): Iterator<SearchResult> =
        results.iterator()
}

fun searchResultsOf(searchQuery: SearchQuery, vararg results: SearchResult) =
    SearchResultCollection(searchQuery, results.toList())
