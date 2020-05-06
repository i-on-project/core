package org.ionproject.core.klass

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KlassController(private val repo: KlassRepo) {

    @GetMapping(Uri.klasses)
    fun getCollection(
        @PathVariable cid: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<Siren> {
        val klasses = repo.getPage(cid, page, limit)

        return ResponseEntity.ok(klasses.toSiren(cid, page, limit))
    }

    @GetMapping(Uri.klassByCalTerm)
    fun get(@PathVariable cid: Int, @PathVariable calterm: String): ResponseEntity<Siren> {
        val klass = repo.get(cid, calterm)

        klass?.let { return ResponseEntity.ok(it.toSiren()) }
        return ResponseEntity.notFound().build()
    }
}
