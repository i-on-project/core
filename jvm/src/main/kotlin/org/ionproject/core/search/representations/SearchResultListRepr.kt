package org.ionproject.core.search.representations

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.search.model.SearchResultCollection

data class SearchItemProperties(
    val id: String,
    val name: String
)

fun SearchResult.toProperties() = SearchItemProperties(id, name)

fun SearchResultCollection.toSearchResultListRepr(pagination: Pagination) =
    SirenBuilder()
        .klass("search", "result", "collection")
        .entities(
            map { it.toSearchResultListItemRepr() }
        )
        .link("self", href = Uri.forSearch(resultOf))
        .link("next", href = Uri.forSearch(resultOf.next()))
        .apply {
            if (pagination.page > 0)
                link("previous", href = Uri.forSearch(resultOf.prev()))
        }
        .toSiren()

fun SearchResult.toSearchResultListItemRepr() =
    SirenBuilder(toProperties())
        .klass(type)
        .rel("item")
        .link("self", href = href)
        .toEmbed()
