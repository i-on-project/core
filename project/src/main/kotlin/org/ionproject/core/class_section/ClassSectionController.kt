package org.ionproject.core.class_section

import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {
    companion object {
        private val CLASS = arrayOf("class", "section")
    }

    @GetMapping("/v0/courses/{acr}/classes/{term}/{id}")
    fun get(@PathVariable acr: String, @PathVariable term: String, @PathVariable id: String): Siren {
        val cs = repo.get(acr, term, id)

        return SirenBuilder(cs)
            .klass(*CLASS)
            .link("self", "/v0/courses/${cs.course}/classes/${cs.calendarTerm}/${cs.id}")
            .link("collection", "/v0/courses/${cs.course}/classes/${cs.calendarTerm}")
            .toSiren()
    }
}
