package org.ionproject.core.search.representations

import org.ionproject.core.common.EmbeddedRepresentation
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.search.model.SearchResult
import org.ionproject.core.search.model.SearchResultCollection

private const val COLLECTION = "collection"
private const val ITEM = "item"
private const val SELF = "self"
private const val SEARCH = "search"
private const val RESULT = "result"

fun SearchResultCollection.toSearchResultListRepr(): Siren =
    SirenBuilder()
        .klass(SEARCH, RESULT, COLLECTION)
        .entities(
            map { it.toSearchResultListItemRepr() }
        )
        .link(SELF, href = Uri.forSearch(resultOf))
            // TODO
        .toSiren()

fun SearchResult.toSearchResultListItemRepr() : EmbeddedRepresentation =
    SirenBuilder(
        mapOf(
            "id" to id,
            "name" to name
        )
    )
        .klass(type, SEARCH, RESULT)
        .rel(ITEM)
        .link(SELF, href = href)
        .toEmbed()