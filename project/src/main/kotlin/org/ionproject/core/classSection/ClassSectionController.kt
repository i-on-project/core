package org.ionproject.core.classSection

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.klass.KlassController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {

    @GetMapping(Uri.classSectionById)
    fun get(@PathVariable acr: String, @PathVariable calterm: String, @PathVariable id: String): Siren {
        val cs = repo.get(acr, calterm, id)
        return ClassSectionOutputModel(cs).toSiren()
    }
}
