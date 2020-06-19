package org.ionproject.core.readApi.search

import org.ionproject.core.readApi.common.transaction.SQLErrors
import org.ionproject.core.readApi.common.transaction.TransactionManager
import org.ionproject.core.readApi.search.model.InvalidSearchQuerySyntaxException
import org.ionproject.core.readApi.search.model.SearchQuery
import org.ionproject.core.readApi.search.model.SearchResultCollection
import org.ionproject.core.readApi.search.sql.SearchData
import org.ionproject.core.readApi.search.sql.SearchData.bindSearchQuery
import org.ionproject.core.readApi.search.sql.SearchResultMapper
import org.jdbi.v3.core.JdbiException
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository

@Repository
class SearchRepoImpl(
    private val tm: TransactionManager,
    private val mapper: SearchResultMapper
) : SearchRepo {
    override fun search(query: SearchQuery): SearchResultCollection {
        val sqlQuery = SearchData.buildSearchQuery(query)

        return tm.run {
            try {
                val searchResults = it.createQuery(sqlQuery)
                    .bindSearchQuery(query)
                    .map(mapper)
                    .list()

                SearchResultCollection(
                    query,
                    searchResults
                )
            } catch (e: JdbiException) {
                val cause = e.cause
                if (cause is PSQLException && cause.sqlState == SQLErrors.SYNTAX_ERROR)
                    throw InvalidSearchQuerySyntaxException(query.query, cause)
                else throw e
            }

        }
    }

}