package org.ionproject.core.search

import org.ionproject.core.search.model.SearchResult
import org.junit.jupiter.api.Test
import java.net.URI

internal class CourseSearchControllerTest : SearchControllerTest() {

    override val results = listOf(
        SearchResult(SearchableEntities.COURSE, "1", "Software Laboratory", URI.create("/api/courses/1")),
        SearchResult(SearchableEntities.CLASS, "4", "SL 1718i", URI.create("/api/courses/1/classes/1718i")),
        SearchResult(SearchableEntities.CLASS, "1", "SL 1718v", URI.create("/api/courses/1/classes/1718v")),
        SearchResult(SearchableEntities.CLASS, "19", "SL 1819i", URI.create("/api/courses/1/classes/1819i")),
        SearchResult(SearchableEntities.CLASS, "7", "SL 1819v", URI.create("/api/courses/1/classes/1819v")),
        SearchResult(SearchableEntities.CLASS, "16", "SL 1920i", URI.create("/api/courses/1/classes/1920i")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1718i 1D", URI.create("/api/courses/1/classes/1718i/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1718i 1N", URI.create("/api/courses/1/classes/1718i/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1718i 2D", URI.create("/api/courses/1/classes/1718i/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1718v 1D", URI.create("/api/courses/1/classes/1718v/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1718v 1N", URI.create("/api/courses/1/classes/1718v/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1718v 2D", URI.create("/api/courses/1/classes/1718v/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1819i 1D", URI.create("/api/courses/1/classes/1819i/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1819i 1N", URI.create("/api/courses/1/classes/1819i/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1819i 2D", URI.create("/api/courses/1/classes/1819i/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1819v 1D", URI.create("/api/courses/1/classes/1819v/1D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1819v 1N", URI.create("/api/courses/1/classes/1819v/1N")),
        SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1819v 2D", URI.create("/api/courses/1/classes/1819v/2D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "LI61D", "SL 1920i LI61D", URI.create("/api/courses/1/classes/1920i/LI61D")),
        SearchResult(SearchableEntities.CLASS_SECTION, "LI61N", "SL 1920i LI61N", URI.create("/api/courses/1/classes/1920i/LI61N"))
    )

    @Test
    fun defaultSearch() {
        test(
            "Lab",
            expectedResults = results.take(4),
            uriBuilder = { search, _, _, _ ->
                URI.create("/api/search?query=$search")
            }
        )
    }
    @Test
    fun searchWithDifferentLimit() {
        test(
            "Lab", 5,
            uriBuilder = { search, limit, _, _ ->
                URI.create("/api/search?query=$search&limit=$limit")
            }
        )
    }
    @Test
    fun searchWithDifferentPage() {
        test(
            "Lab",
            page = 1,
            expectedResults = emptyList(),
            uriBuilder = { search, _, page, _ ->
                URI.create("/api/search?query=$search&page=$page")
            }
        )
    }
    @Test
    fun searchWithDifferentLimitAndPage() {
        test(
            "Lab",
            1,
            1,
            expectedResults = results.drop(1).take(1),
            uriBuilder = { search, limit, page, _ ->
                URI.create("/api/search?query=$search&page=$page&limit=$limit")
            }
        )
    }
    @Test
    fun searchWithDifferentTypes() {
        test(
            "Lab",
            expectedResults = results.take(1),
            types = listOf(SearchableEntities.PROGRAMME, SearchableEntities.COURSE, SearchableEntities.CLASS_SECTION),
            uriBuilder = { search, _, _, types ->
                URI.create("/api/search?query=$search&types=${types.joinToString(",")}")
            }
        )
    }
}
