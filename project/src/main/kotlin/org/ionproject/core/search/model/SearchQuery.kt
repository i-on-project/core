package org.ionproject.core.search.model

import org.ionproject.core.search.SearchableEntity

class SearchQuery private constructor(
    val query: String,
    val types: List<SearchableEntity>,
    val limit: Int,
    val page: Int
) {
    companion object {
        operator fun invoke(query: String, types: List<String>, limit: Int, page: Int) : SearchQuery {

            val validTypes = types.map {
                try {
                    SearchableEntity.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    throw InvalidSearchTypeException(it)
                }
            }

            return SearchQuery(
                wildcard(query), validTypes, limit, page
            )
        }

        private fun wildcard(orig: String) : String {
            return orig.replace(" ",":* ")
        }
    }
}

class InvalidSearchTypeException(val type: String) : Exception("An unsupported type was used.")
