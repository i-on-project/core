package org.ionproject.core.klass

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class KlassController(private val repo: KlassRepo) {

    @GetMapping(Uri.klassByTerm)
    fun get(@PathVariable acr: String, @PathVariable calterm: String): Siren {
        val klass: FullKlass = repo.get(acr, calterm)
        return FullKlassOutputModel(klass).toSiren()
    }

    @GetMapping(Uri.klasses)
    fun getCollection(@PathVariable acr: String): Siren {
        val klass = repo.getPage(acr, 0, 3)
        return KlassCollectionOutputModel(acr, klass).toSiren()
    }
}
