package org.ionproject.core.search.model

import org.ionproject.core.search.SearchableEntity

class SearchQuery private constructor(
    val query: String,
    val types: List<SearchableEntity>,
    val limit: Int,
    val page: Int
) {

    companion object {
        operator fun invoke(query: String, types: List<String>, limit: Int, page: Int): SearchQuery {
            if (types.isEmpty()) throw IllegalArgumentException("There must [types] defined for a search query.")
            return SearchQuery(
                query, types.map { SearchableEntity.parse(it) }, limit, page
            )
        }
    }
}

class InvalidSearchTypeException(val type: String) : Exception("An unsupported type was used.")
