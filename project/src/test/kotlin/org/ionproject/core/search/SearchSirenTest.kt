package org.ionproject.core.search

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.search.model.SearchResultCollection
import org.ionproject.core.search.representations.toSearchResultListRepr
import org.ionproject.core.utils.assertSiren
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val COLLECTION = "collection"
private const val ITEM = "item"
private const val SELF = "self"
private const val SEARCH = "search"
private const val RESULT = "result"
private const val ID = "id"
private const val NAME = "name"

class SearchSirenTest {

    private companion object {
        val genericQuery = SearchQuery(
            "Test",
            SearchableEntities.ALL,
            3,
            1
        )

        val genericResults = listOf<SearchResult>(
            SearchResult(SearchableEntities.CALENDAR_TERM, "1920v", "1920v", Uri.forCalTermById("1920v")),
            SearchResult(SearchableEntities.PROGRAMME, "321", "Mestrado em Engenharia Informática", Uri.forProgrammesById(321)),
            SearchResult(SearchableEntities.COURSE, "5456", "Mathematics II", Uri.forCourseById(5456))
        )

        val genericSiren = buildSiren(genericQuery, genericResults)

        val noResultsQuery = SearchQuery(
            "Test",
            SearchableEntities.ALL,
            3,
            1
        )

        val noResultsResults = listOf<SearchResult>()

        val noResultsSiren = buildSiren(noResultsQuery, noResultsResults)

        fun buildSiren(searchQuery: SearchQuery, results: List<SearchResult>) =
            SirenBuilder()
                .klass(SEARCH, RESULT, COLLECTION)
                .entities(
                    results.map {
                        SirenBuilder(
                            mapOf(
                                ID to it.id,
                                NAME to it.name
                            )
                        )
                            .klass(it.type, SEARCH, RESULT)
                            .rel(ITEM)
                            .link(SELF, href = it.href)
                            .toEmbed()
                    }
                )
                .link(SELF, href = Uri.forSearch(searchQuery))
                .toSiren()
    }

    @Test
    fun genericSearchResult() {
        val collection = SearchResultCollection(
            genericQuery,
            genericResults
        )

        assertSiren(collection.toSearchResultListRepr(), genericSiren)
    }

    @Test
    fun noResultsSearchResult() {
        val collection = SearchResultCollection(
            noResultsQuery,
            noResultsResults
        )

        assertSiren(collection.toSearchResultListRepr(), noResultsSiren)
    }
}