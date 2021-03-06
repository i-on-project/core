package org.ionproject.core.search.sql

import org.ionproject.core.search.SearchableEntity
import org.ionproject.core.search.model.SearchQuery
import org.jdbi.v3.core.statement.Query

object SearchData {
    const val TYPE = "type"
    const val ID = "id"
    const val NAME = "name"
    const val HREF = "href"
    const val RANK = "rank"
    const val QUERY = "(SELECT * FROM q)"

    fun buildSearchQuery(searchQuery: SearchQuery): String =
        buildString {
            appendLine(" WITH q AS ( SELECT to_tsquery(:query) )")

            appendLine(
                searchQuery.types.map {
                    SearchableEntity.QUERY_MAP[it]
                }.joinToString("UNION\n")
            )

            appendLine("order by $RANK desc, $HREF asc")
            appendLine("OFFSET :offset")
            appendLine("LIMIT :limit")
        }

    fun Query.bindSearchQuery(searchQuery: SearchQuery): Query {
        bind("query", searchQuery.query.replace(" ", ":* | ") + ":*")
        bind("offset", (searchQuery.page) * searchQuery.limit)
        bind("limit", searchQuery.limit)

        return this
    }
}
