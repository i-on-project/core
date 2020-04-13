package org.ionproject.core.classSection

import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {

    @GetMapping(Uri.classSectionById, produces = [Media.SIREN_TYPE])
    fun get(@PathVariable cid: Int, @PathVariable calterm: String, @PathVariable sid: String): Siren {
        val cs = repo.get(cid, calterm, sid)
        return ClassSectionToSiren.toSiren(cs)
    }
}
