package org.ionproject.core.search

import org.ionproject.core.common.Media
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
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
        uriBuilder: (search: String, limit: Int, page: Int, types: List<String>) -> URI)
    {
        val expected = SearchSirenTest.buildSiren(search, limit, page, types, expectedResults)

        mocker.get(uriBuilder(search, limit, page, types))
            .andDo { print() }
            .andExpect {
                status { isOk }
                header {
                    val contentType = "content-type"
                    exists(contentType)
                    stringValues(contentType, Media.SIREN_TYPE)
                }
                expected.matchMvc(this)
            }
            .andReturn()
    }
}