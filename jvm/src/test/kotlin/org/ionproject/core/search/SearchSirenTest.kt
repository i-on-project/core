package org.ionproject.core.search

import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.search.model.SearchResultCollection
import org.ionproject.core.search.representations.toProperties
import org.ionproject.core.search.representations.toSearchResultListRepr
import org.ionproject.core.utils.assertSiren
import org.junit.jupiter.api.Test
import java.net.URI

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

        private fun buildSiren(searchQuery: SearchQuery, results: List<SearchResult>): Siren {
            return buildSiren(searchQuery.query, searchQuery.limit, searchQuery.page, searchQuery.types.map { it.toString() }, results)
        }

        fun buildSiren(search: String, limit: Int, page: Int, types: List<String>, results: List<SearchResult>): Siren =
            SirenBuilder()
                .klass("search", "result", "collection")
                .entities(
                    results.map {
                        SirenBuilder(it.toProperties())
                            .klass(it.type)
                            .rel("item")
                            .link("self", href = it.href)
                            .toEmbed()
                    }
                )
                .link("self", href = URI.create("${Uri.baseUrl}/api/search?query=$search&types=${types.joinToString(",")}&limit=$limit&page=$page"))
                .link("next", href = URI.create("${Uri.baseUrl}/api/search?query=$search&types=${types.joinToString(",")}&limit=$limit&page=${page + 1}"))
                .apply {
                    if (page > 0)
                        link("previous", href = URI.create("${Uri.baseUrl}/api/search?query=$search&types=${types.joinToString(",")}&limit=$limit&page=${page - 1}"))
                }
                .toSiren()
    }

    @Test
    fun genericSearchResult() {
        val collection = SearchResultCollection(
            genericQuery,
            genericResults
        )

        val pagination = Pagination(1, 3)
        assertSiren(collection.toSearchResultListRepr(pagination), genericSiren)
    }

    @Test
    fun noResultsSearchResult() {
        val collection = SearchResultCollection(
            noResultsQuery,
            noResultsResults
        )

        val pagination = Pagination(1, 3)
        assertSiren(collection.toSearchResultListRepr(pagination), noResultsSiren)
    }
}
