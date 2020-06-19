package org.ionproject.core.readApi.search.model

import java.net.URI

data class SearchResult(
    val type: String,
    val id: String,
    val name: String,
    val href: URI
) {
    constructor(
        type: String,
        id: String,
        name: String,
        href: String
    ) : this(type, id, name, URI.create(href))
}
