package org.ionproject.core.search

import org.ionproject.core.search.model.SearchResult
import org.junit.jupiter.api.Test
import java.net.URI

internal class ClassSearchControllerTest : SearchControllerTest() {
    val searchQuery = "Lab%201920i"

    override val results: List<SearchResult> = listOf(
        SearchResult(SearchableEntities.CLASS, "16", "SL 1920i", "/api/courses/1/classes/1920i"),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1920i 1D", "/api/courses/1/classes/1920i/1D"),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1920i 1N", "/api/courses/1/classes/1920i/1N"),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1920i 2D", "/api/courses/1/classes/1920i/2D"),
        SearchResult(SearchableEntities.CALENDAR_TERM, "1920i", "1920i", "/api/calendar-terms/1920i"),
        SearchResult(SearchableEntities.COURSE, "1", "Software Laboratory", "/api/courses/1"),
        SearchResult(SearchableEntities.CLASS, "4", "SL 1718i", "/api/courses/1/classes/1718i"),
        SearchResult(SearchableEntities.CLASS, "1", "SL 1718v", "/api/courses/1/classes/1718v"),
        SearchResult(SearchableEntities.CLASS, "19", "SL 1819i", "/api/courses/1/classes/1819i"),
        SearchResult(SearchableEntities.CLASS, "7", "SL 1819v", "/api/courses/1/classes/1819v"),
        SearchResult(SearchableEntities.CLASS, "17", "WAD 1920i", "/api/courses/2/classes/1920i"),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "WAD 1920i 1D", "/api/courses/2/classes/1920i/1D"),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "WAD 1920i 1N", "/api/courses/2/classes/1920i/1N"),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "WAD 1920i 2D", "/api/courses/2/classes/1920i/2D"),
        SearchResult(SearchableEntities.CLASS, "18", "DM 1920i", "/api/courses/3/classes/1920i"),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "DM 1920i 1D", "/api/courses/3/classes/1920i/1D"),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "DM 1920i 1N", "/api/courses/3/classes/1920i/1N"),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "DM 1920i 2D", "/api/courses/3/classes/1920i/2D")
    )

    @Test
    fun defaultSearch() {
        test(
            uriBuilder = { search, _, _, _ ->
                URI.create("/api/search?query=$search")
            }
        )
    }
    @Test
    fun searchWithDifferentLimit() {
        test(
            5,
            uriBuilder = { search, limit, _, _ ->
                URI.create("/api/search?query=$search&limit=$limit")
            }
        )
    }
    @Test
    fun searchWithDifferentPage() {
        test(
            page = 1,
            uriBuilder = { search, _, page, _ ->
                URI.create("/api/search?query=$search&page=$page")
            }
        )
    }
    @Test
    fun searchWithDifferentLimitAndPage() {
        test(
            5, 1,
            uriBuilder = { search, limit, page, _ ->
                URI.create("/api/search?query=$search&page=$page&limit=$limit")
            }
        )
    }
    @Test
    fun searchWithDifferentTypes() {
        test(
            types = listOf(SearchableEntities.PROGRAMME, SearchableEntities.COURSE, SearchableEntities.CLASS_SECTION),
            uriBuilder = { search, _, _, types ->
                URI.create("/api/search?query=$search&types=${types.joinToString(",")}")
            }
        )
    }

    fun test(
        limit: Int = defaultLimit,
        page: Int = defaultPage,
        types: List<String> = defaultTypes,
        expectedResults: List<SearchResult> = defaultResults(limit, page, types),
        uriBuilder: (search: String, limit: Int, page: Int, types: List<String>) -> URI
    ) {
        super.test(searchQuery, limit, page, types, expectedResults, uriBuilder)
    }
}
