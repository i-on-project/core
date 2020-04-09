package org.ionproject.core.classSection

import org.ionproject.core.common.SIREN_MEDIA_TYPE
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.klass.KlassController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {

    @ExceptionHandler
    fun handleClassNotInDbException(exception: ClassSectionNotInDbException) =
        ResponseEntity.notFound().build<Any>()

    @GetMapping(Uri.classSectionById, produces = [SIREN_MEDIA_TYPE], headers = [ "Accept=application/json"])
    fun get(@PathVariable cid: Int, @PathVariable calterm: String, @PathVariable sid: String): Siren {
        val cs = repo.get(cid, calterm, sid)
        return ClassSectionToSiren.toSiren(cs)
    }
}
