package org.ionproject.core.readApi.search.representations

import org.ionproject.core.readApi.common.EmbeddedRepresentation
import org.ionproject.core.readApi.common.Siren
import org.ionproject.core.readApi.common.SirenBuilder
import org.ionproject.core.readApi.common.Uri
import org.ionproject.core.readApi.search.model.SearchResult
import org.ionproject.core.readApi.search.model.SearchResultCollection

private const val COLLECTION = "collection"
private const val ITEM = "item"
private const val SELF = "self"
private const val SEARCH = "search"
private const val RESULT = "result"
private const val ID = "id"
private const val NAME = "name"

fun SearchResultCollection.toSearchResultListRepr(): Siren =
    SirenBuilder()
        .klass(SEARCH, RESULT, COLLECTION)
        .entities(
            map { it.toSearchResultListItemRepr() }
        )
        .link(SELF, href = Uri.forSearch(resultOf))
        // TODO("Next and previous relations")
        .toSiren()

fun SearchResult.toSearchResultListItemRepr(): EmbeddedRepresentation =
    SirenBuilder(
        mapOf(
            ID to id,
            NAME to name
        )
    )
        .klass(type)
        .rel(ITEM)
        .link(SELF, href = href)
        .toEmbed()