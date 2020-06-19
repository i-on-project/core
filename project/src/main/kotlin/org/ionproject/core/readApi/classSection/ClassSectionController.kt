package org.ionproject.core.readApi.classSection

import org.ionproject.core.readApi.common.Siren
import org.ionproject.core.readApi.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {

    @GetMapping(Uri.classSectionById)
    fun get(
        @PathVariable cid: Int,
        @PathVariable calterm: String,
        @PathVariable sid: String
    ): ResponseEntity<Siren> {

        val cs = repo.get(cid, calterm, sid)

        cs?.let { return ResponseEntity.ok(it.toSiren()) }
        return ResponseEntity.notFound().build()
    }
}
