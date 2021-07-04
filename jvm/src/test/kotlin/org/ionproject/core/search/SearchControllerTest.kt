package org.ionproject.core.search

import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import java.net.URI

internal abstract class SearchControllerTest : ControllerTester() {

    abstract val results: List<SearchResult>

    companion object {
        const val defaultLimit = 10
        const val defaultPage = 0
        val defaultTypes = SearchableEntity.ALL.map { it.toString() }
    }

    fun defaultResults(limit: Int, page: Int, types: List<String>): List<SearchResult> {
        return results.filter { types.contains(it.type) }.drop((page) * limit).take(limit)
    }

    fun test(
        search: String,
        limit: Int = defaultLimit,
        page: Int = defaultPage,
        types: List<String> = defaultTypes,
        expectedResults: List<SearchResult> = defaultResults(limit, page, types),
        uriBuilder: (search: String, limit: Int, page: Int, types: List<String>) -> URI
    ) {
        val expected = SearchSirenTest.buildSiren(search, limit, page, types, expectedResults)

        isValidSiren(uriBuilder(search, limit, page, types))
            .andDo { println() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
