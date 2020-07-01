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