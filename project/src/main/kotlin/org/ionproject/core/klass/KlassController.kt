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
    fun get(@PathVariable cid: Int, @PathVariable calterm: String): Siren {
        val klass: FullKlass = repo.get(cid, calterm)

        return KlassToSiren.toSiren(klass)
    }

    @GetMapping(Uri.klasses)
    fun getCollection(@PathVariable cid: Int,
                      @RequestParam(defaultValue = "0") page: Int,
                      @RequestParam(defaultValue = "5") size: Int): Siren {
        val klass = repo.getPage(cid, page, size)

        return KlassToSiren.toSiren(cid, klass, page, size)
    }
}
