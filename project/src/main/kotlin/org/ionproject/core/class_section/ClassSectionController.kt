package org.ionproject.core.class_section

import org.ionproject.core.common.Action
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.course_instance.CourseInstanceController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ClassSectionController(private val repo: ClassSectionRepo) {
    companion object {
        private val CLASS = arrayOf("class", "section")

        fun buildHref(acr: String, term: String, id: String) =
                "${CourseInstanceController.buildHref(acr, term)}/${id}"
    }

    @GetMapping("/v0/courses/{acr}/classes/{term}/{id}")
    fun get(@PathVariable acr: String, @PathVariable term: String, @PathVariable id: String): Siren {
        val cs = repo.get(acr, term, id)

        val selfHref = buildHref(cs.course, cs.calendarTerm, cs.id)
        return SirenBuilder(cs)
                .klass(*CLASS)
                .link("self", URI(selfHref))
                .link("collection", URI(CourseInstanceController.buildHref(cs.course, cs.calendarTerm)))
                .action(Action.genDeleteAction(URI(selfHref)))
                .toSiren()
    }
}
