package org.ionproject.core.search

import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.search.model.SearchResultCollection
import org.ionproject.core.search.representations.toSearchResultListRepr
import org.ionproject.core.utils.assertSiren
import org.junit.jupiter.api.Test
import java.net.URI

const val COLLECTION = "collection"
const val ITEM = "item"
const val SELF = "self"
const val SEARCH = "search"
const val RESULT = "result"
const val ID = "id"
const val NAME = "name"

class SearchSirenTest {

    companion object {
        val genericQuery = SearchQuery(
            "Test",
            SearchableEntity.ALL.map { it.toString() },
            3,
            1
        )

        val genericResults = listOf(
            SearchResult(SearchableEntities.CALENDAR_TERM, "1920v", "1920v", Uri.forCalTermById("1920v")),
            SearchResult(SearchableEntities.PROGRAMME, "321", "Mestrado em Engenharia Informática", Uri.forProgrammesById(321)),
            SearchResult(SearchableEntities.COURSE, "5456", "Mathematics II", Uri.forCourseById(5456))
        )

        val genericSiren = buildSiren(genericQuery, genericResults)

        val noResultsQuery = SearchQuery(
            "Test",
            SearchableEntity.ALL.map { it.toString() },
            3,
            1
        )

        val noResultsResults = listOf<SearchResult>()

        val noResultsSiren = buildSiren(noResultsQuery, noResultsResults)

        fun buildSiren(searchQuery: SearchQuery, results: List<SearchResult>): Siren {
            return buildSiren(searchQuery.query, searchQuery.limit, searchQuery.page, searchQuery.types.map { it.toString() }, results)
        }

        fun buildSiren(search: String, limit: Int, page: Int, types: List<String>, results: List<SearchResult>): Siren =
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
                            .klass(it.type)
                            .rel(ITEM)
                            .link(SELF, href = it.href)
                            .toEmbed()
                    }
                )
                .link(SELF, href = URI.create("/v0/search?query=$search&types=${types.joinToString(",")}&limit=$limit&page=$page"))
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
