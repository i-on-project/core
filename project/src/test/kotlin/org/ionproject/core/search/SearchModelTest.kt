package org.ionproject.core.search

import org.ionproject.core.search.model.InvalidSearchTypeException
import org.ionproject.core.search.model.SearchQuery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class SearchModelTest {

    @Test
    fun invalidSearchType() {
        val query = ""
        val types = listOf(SearchableEntities.PROGRAMME, "invalid-type")
        val limit = 0
        val page = 0

        try {
            SearchQuery(
                query,
                types,
                limit,
                page
            )
            fail("The invalid type was accepted.")
        } catch (e: Exception) {
            assertEquals(InvalidSearchTypeException::class.java, e.javaClass, "The exception was not of type [InvalidSearchTypeException]")
        }
    }

    @Test
    fun validSearchQuery() {
        val query = "some query"
        val types = SearchableEntity.ALL.map { it.toString() }
        val limit = 0
        val page = 0

        SearchQuery(
            query,
            types,
            limit,
            page
        )

        assertTrue(true)
    }
}
