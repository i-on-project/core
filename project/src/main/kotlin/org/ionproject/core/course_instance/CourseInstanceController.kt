package org.ionproject.core.course_instance

import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CourseInstanceController(private val repo: CourseInstanceRepo) {

    private val CLASS = arrayOf("class")

    @GetMapping("/v0/courses/{acr}/classes/{term}")
    fun get(@PathVariable acr: String, @PathVariable term: String): Siren {
        val ci = repo.get(acr, term)

        return SirenBuilder(ci)
            .klass(*CLASS)
            .link("self", "/v0/courses/${ci.course}/classes/${ci.calendarTerm}")
            .toSiren()
    }

    @GetMapping("/v0/courses/{acr}/classes")
    fun getCollection(@PathVariable acr: String): Siren {
        val ci = repo.getMany(acr)

        // transforms all CourseInstances into Siren embeds
        val embeds = ci.map {
            SirenBuilder(it)
                .klass(*CLASS)
                .rel("item")
                .link("self", "/v0/courses/${it.course}/classes/${it.calendarTerm}")
                .toEmbed()
        }.toTypedArray()

        return SirenBuilder(Unit) // unit has empty properties
            .klass(*CLASS, "collection")
            .entities(*embeds)
            .link("self", "/v0/courses/${acr}/classes")
            .toSiren()
    }
}
