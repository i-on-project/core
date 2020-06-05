package org.ionproject.core.search.model

import org.ionproject.core.search.SearchableEntity

class SearchQuery private constructor(
    val query: String,
    val types: List<SearchableEntity>,
    val limit: Int,
    val page: Int
) {
    val args: List<String> by lazy {
        query.split(' ').filter{ it.isNotEmpty() }.map { wildcard(it) }
    }

    companion object {
        operator fun invoke(query: String, types: List<String>, limit: Int, page: Int) : SearchQuery =
            SearchQuery(
                query, types.map { SearchableEntity.parse(it) }, limit, page
            )

        private fun wildcard(orig: String) : String {
            return "$orig:*"
        }
    }
}

class InvalidSearchTypeException(val type: String) : Exception("An unsupported type was used.")
