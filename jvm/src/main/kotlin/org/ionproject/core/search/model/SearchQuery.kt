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

    fun next() = SearchQuery(query, types, limit, page + 1)
    fun prev() = SearchQuery(query, types, limit, page - 1)
}

class InvalidSearchTypeException(val type: String) : IllegalArgumentException("An unsupported type was used.")

class InvalidSearchQuerySyntaxException : IllegalArgumentException {

    companion object {
        private val errorMessage = "The supplied query has invalid syntax."
    }

    val query: String

    constructor(query: String, cause: Throwable) : super(errorMessage, cause) {
        this.query = query
    }

    constructor(query: String) : super(errorMessage) {
        this.query = query
    }
}
