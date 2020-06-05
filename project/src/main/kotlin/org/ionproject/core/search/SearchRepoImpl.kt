package org.ionproject.core.search

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.model.SearchResultCollection
import org.ionproject.core.search.sql.SearchData
import org.ionproject.core.search.sql.SearchData.bindSearchQuery
import org.ionproject.core.search.sql.SearchResultMapper
import org.springframework.stereotype.Repository

@Repository
class SearchRepoImpl(
    private val tm: TransactionManager,
    private val mapper: SearchResultMapper
) : SearchRepo {
    override fun search(query: SearchQuery): SearchResultCollection {
        val sqlQuery = SearchData.buildSearchQuery(query)

        return tm.run {
            val searchResults = it.createQuery(sqlQuery)
                .bindSearchQuery(query)
                .map(mapper)
                .list()

            SearchResultCollection(
                query,
                searchResults
            )
        }
    }

}