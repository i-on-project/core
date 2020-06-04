package org.ionproject.core.search.model

import java.net.URI

data class SearchResult(
    val type: String,
    val id: String,
    val name: String,
    val href: URI
)
