package org.ionproject.core.common.argumentResolvers.parameters

import com.fasterxml.jackson.annotation.JsonIgnore

data class Pagination(
    val page: Int,
    val limit: Int
) {
    @JsonIgnore
    val offset = page * limit
}
