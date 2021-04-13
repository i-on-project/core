package org.ionproject.core.klass

import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.PaginationDefaults
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class KlassController(private val repo: KlassRepo) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_CLASSES, ResourceIds.VERSION_0)
    @GetMapping(Uri.klasses)
    fun getCollection(
        @PathVariable cid: Int,
        @PaginationDefaults(defaultLimit = 5) pagination: Pagination
    ): ResponseEntity<Siren> {
        val klasses = repo.getPage(cid, pagination.page, pagination.limit)
        return ResponseEntity.ok(klasses.toSiren(cid, pagination.page, pagination.limit))
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_CLASS, ResourceIds.VERSION_0)
    @GetMapping(Uri.klassByCalTerm)
    fun get(@PathVariable cid: Int, @PathVariable calterm: String): ResponseEntity<Siren> {
        val klass = repo.get(cid, calterm)

        klass?.let { return ResponseEntity.ok(it.toSiren()) }
        return ResponseEntity.notFound().build()
    }
}
