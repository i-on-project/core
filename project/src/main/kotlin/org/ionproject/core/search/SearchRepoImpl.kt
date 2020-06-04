package org.ionproject.core.search

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.model.SearchResultCollection
import org.ionproject.core.search.sql.SearchResultMapper
import org.springframework.stereotype.Repository

@Repository
class SearchRepoImpl(
    private val tm: TransactionManager,
    private val mapper: SearchResultMapper
) : SearchRepo {
    override fun search(query: SearchQuery): SearchResultCollection {
        val sqlQuery = query.types.map { SearchableEntity.QUERY_MAP[it] }.joinToString("\nUNION\n")

        return tm.run<SearchResultCollection> {
            TODO("Use tsquery variable instead of having to_tsquery in the query")
        }
    }

}