package org.ionproject.core.classSection

import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_CLASS_SECTION, ResourceIds.VERSION_0)
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
