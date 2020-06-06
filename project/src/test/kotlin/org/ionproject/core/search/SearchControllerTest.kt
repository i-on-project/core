package org.ionproject.core.search

import org.ionproject.core.common.Media
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import java.net.URI

internal class SearchControllerTest : ControllerTester() {

    companion object {
        const val defaultLimit = 10
        const val defaultPage = 1
        val results = listOf(
            SearchResult(SearchableEntities.CLASS, "4", "SL 1718i", URI.create("/v0/courses/1/classes/1718i")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1718i 1D", URI.create("/v0/courses/1/classes/1718i/1D")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1718i 1N", URI.create("/v0/courses/1/classes/1718i/1N")),
            SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1718i 2D", URI.create("/v0/courses/1/classes/1718i/2D")),
            SearchResult(SearchableEntities.CLASS, "1", "SL 1718v", URI.create("/v0/courses/1/classes/1718v")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1718v 1D", URI.create("/v0/courses/1/classes/1718v/1D")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1718v 1N", URI.create("/v0/courses/1/classes/1718v/1N")),
            SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1718v 2D", URI.create("/v0/courses/1/classes/1718v/2D")),
            SearchResult(SearchableEntities.CLASS, "19", "SL 1819i", URI.create("/v0/courses/1/classes/1819i")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1819i 1D", URI.create("/v0/courses/1/classes/1819i/1D")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1819i 1N", URI.create("/v0/courses/1/classes/1819i/1N")),
            SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1819i 2D", URI.create("/v0/courses/1/classes/1819i/2D")),
            SearchResult(SearchableEntities.CLASS, "7", "SL 1819v", URI.create("/v0/courses/1/classes/1819v")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1819v 1D", URI.create("/v0/courses/1/classes/1819v/1D")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1819v 1N", URI.create("/v0/courses/1/classes/1819v/1N")),
            SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1819v 2D", URI.create("/v0/courses/1/classes/1819v/2D")),
            SearchResult(SearchableEntities.CLASS, "16", "SL 1920i", URI.create("/v0/courses/1/classes/1920i")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1D", "SL 1920i 1D", URI.create("/v0/courses/1/classes/1920i/1D")),
            SearchResult(SearchableEntities.CLASS_SECTION, "1N", "SL 1920i 1N", URI.create("/v0/courses/1/classes/1920i/1N")),
            SearchResult(SearchableEntities.CLASS_SECTION, "2D", "SL 1920i 2D", URI.create("/v0/courses/1/classes/1920i/2D"))
        )
    }

    @Test
    fun defaultSearch() {
        test("Lab", uriBuilder = { search, _, _, _ ->
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
        test("Lab", page = 2, uriBuilder = { search, _, page, _ ->
            URI.create("/v0/search?query=$search&page=$page")
        })
    }

    @Test
    fun searchWithDifferentLimitAndPage() {
        test("Lab", 5, 2, uriBuilder = { search, limit, page, _ ->
            URI.create("/v0/search?query=$search&page=$page&limit=$limit")
        })
    }

    @Test
    fun searchWithDifferentTypes() {
        test("Lab", types = listOf(SearchableEntities.PROGRAMME, SearchableEntities.COURSE, SearchableEntities.CLASS_SECTION), uriBuilder = { search, _, _, types ->
            URI.create("/v0/search?query=$search&types=${types.joinToString(",")}")
        })
    }

    fun test(
        search: String,
        limit: Int = defaultLimit,
        page: Int = defaultPage,
        types: List<String> = SearchableEntities.ALL,
        uriBuilder: (search: String, limit: Int, page: Int, types: List<String>) -> URI)
    {
        val expected = SirenBuilder()
            .klass(SEARCH, RESULT, COLLECTION)
            .entities(
                results.filter { types.contains(it.type) }.drop((page - 1) * limit).take(limit).map {
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
            .link(SELF, href = URI.create("/v0/search?query=$search&types=${types.joinToString(",")}&limit=$limit&page=$page"))
            .toSiren()

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