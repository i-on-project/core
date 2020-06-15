package org.ionproject.core.search

import org.ionproject.core.search.model.SearchResult
import org.junit.jupiter.api.Test
import java.net.URI

internal class CourseSearchControllerTest : SearchControllerTest() {

    override val results = listOf(
        SearchResult(SearchableEntities.COURSE, "1", "Software Laboratory", URI.create("/v0/courses/1")),
        SearchResult(SearchableEntities.CLASS, "4", "SL 1718i", URI.create("/v0/courses/1/classes/1718i")),
        SearchResult(SearchableEntities.CLASS, "1", "SL 1718v", URI.create("/v0/courses/1/classes/1718v")),
        SearchResult(SearchableEntities.CLASS, "19", "SL 1819i", URI.create("/v0/courses/1/classes/1819i")),
        SearchResult(SearchableEntities.CLASS, "7", "SL 1819v", URI.create("/v0/courses/1/classes/1819v")),
        SearchResult(SearchableEntities.CLASS, "16", "SL 1920i", URI.create("/v0/courses/1/classes/1920i")),
        SearchResult(SearchableEntities.CLASS, "22", "SL 2021i", URI.create("/v0/courses/1/classes/2021i")),
        SearchResult(SearchableEntities.CLASS, "19", "SL 2021v", URI.create("/v0/courses/1/classes/2021v")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1718i 1D", URI.create("/v0/courses/1/classes/1718i/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1718i 1N", URI.create("/v0/courses/1/classes/1718i/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1718i 2D", URI.create("/v0/courses/1/classes/1718i/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1718v 1D", URI.create("/v0/courses/1/classes/1718v/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1718v 1N", URI.create("/v0/courses/1/classes/1718v/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1718v 2D", URI.create("/v0/courses/1/classes/1718v/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1819i 1D", URI.create("/v0/courses/1/classes/1819i/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1819i 1N", URI.create("/v0/courses/1/classes/1819i/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1819i 2D", URI.create("/v0/courses/1/classes/1819i/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1819v 1D", URI.create("/v0/courses/1/classes/1819v/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1819v 1N", URI.create("/v0/courses/1/classes/1819v/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1819v 2D", URI.create("/v0/courses/1/classes/1819v/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1920i 1D", URI.create("/v0/courses/1/classes/1920i/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1920i 1N", URI.create("/v0/courses/1/classes/1920i/1N"))
    )

    @Test
    fun defaultSearch() {
        test("Lab", expectedResults = results.take(8), uriBuilder = { search, _, _, _ ->
            URI.create("/v0/search?query=$search")
        })
    }
    @Test
    fun searchWithDifferentLimit() {
        test("Lab", 5, uriBuilder = { search, limit, _, _ ->
            URI.create("/v0/search?query=$search&limit=$limit")
        })
    }
    @Test
    fun searchWithDifferentPage() {
        test("Lab", page = 2, expectedResults = emptyList(), uriBuilder = { search, _, page, _ ->
            URI.create("/v0/search?query=$search&page=$page")
        })
    }
    @Test
    fun searchWithDifferentLimitAndPage() {
        test("Lab", 5, 2, expectedResults = results.drop(5).take(3), uriBuilder = { search, limit, page, _ ->
            URI.create("/v0/search?query=$search&page=$page&limit=$limit")
        })
    }
    @Test
    fun searchWithDifferentTypes() {
        test("Lab", expectedResults = results.take(1), types = listOf(SearchableEntities.PROGRAMME, SearchableEntities.COURSE, SearchableEntities.CLASS_SECTION), uriBuilder = { search, _, _, types ->
            URI.create("/v0/search?query=$search&types=${types.joinToString(",")}")
        })
    }
}