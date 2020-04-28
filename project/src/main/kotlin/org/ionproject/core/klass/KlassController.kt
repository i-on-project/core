package org.ionproject.core.klass

import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.klass.model.FullKlass
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KlassController(private val repo: KlassRepo) {

    @GetMapping(Uri.klassByCalTerm, produces = [Media.SIREN_TYPE])
    fun get(@PathVariable cid: Int, @PathVariable calterm: String): Siren {
        val klass: FullKlass = repo.get(cid, calterm)

        return klass.toSiren()
    }

    @GetMapping(Uri.klasses, produces = [Media.SIREN_TYPE])
    fun getCollection(@PathVariable cid: Int,
                      @RequestParam(defaultValue = "0") page: Int,
                      @RequestParam(defaultValue = "5") limit: Int): Siren {
        val klass = repo.getPage(cid, page, limit)

        return klass.toSiren(cid, page, limit)
    }
}
