package org.ionproject.core.klass

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class KlassController(private val repo: KlassRepo) {

    @ExceptionHandler
    fun handleClassNotInDbException(exception: ClassNotInDbException) =
        ResponseEntity.notFound().build<Any>()

    @GetMapping(Uri.klassByTerm)
    fun get(@PathVariable acr: String, @PathVariable calterm: String): Siren {
        val klass: FullKlass = repo.get(acr, calterm)

        return KlassToSiren.toSiren(klass)
    }

    @GetMapping(Uri.klasses)
    fun getCollection(@PathVariable acr: String,
                      @RequestParam(defaultValue = "0") page: Int,
                      @RequestParam(defaultValue = "5") size: Int): Siren {
        val klass = repo.getPage(acr, page, size)

        return KlassToSiren.toSiren(acr, klass, page, size)
    }
}
