package org.ionproject.core.readApi.search.sql

import org.ionproject.core.readApi.search.model.SearchResult
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.net.URI
import java.sql.ResultSet

@Component
class SearchResultMapper : RowMapper<SearchResult> {
    override fun map(rs: ResultSet, ctx: StatementContext): SearchResult =
        SearchResult(
            rs.getString(SearchData.TYPE),
            rs.getString(SearchData.ID),
            rs.getString(SearchData.NAME),
            URI.create(rs.getString(SearchData.HREF))
        )
}