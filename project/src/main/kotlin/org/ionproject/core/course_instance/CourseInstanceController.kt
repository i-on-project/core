package org.ionproject.core.course_instance

import org.ionproject.core.class_section.ClassSectionController
import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CourseInstanceController(private val repo: CourseInstanceRepo) {
    companion object {
        private val CLASS = arrayOf("class")

        fun buildHref(acr: String, term: String) = "/v0/courses/${acr}/classes/${term}"
        fun buildHref(acr: String) = "/v0/courses/${acr}/classes"
    }

    @GetMapping("/v0/courses/{acr}/classes/{term}")
    fun get(@PathVariable acr: String, @PathVariable term: String): Siren {
        val ci = repo.get(acr, term)
        val selfHref = buildHref(ci.course, ci.calendarTerm)

        val sections = ci.sections.map {
            SirenBuilder(it)
                    .klass("class", "section")
                    .rel("item")
                    .link("self", URI(ClassSectionController.buildHref(ci.course, ci.calendarTerm, it.id)))
                    .toEmbed()
        }

        return SirenBuilder(ci)
                .klass(*CLASS)
                .entities(sections)
                .link("self", URI(selfHref))
                .link("collection", URI("/v0/courses/${ci.course}/classes"))
                .action(Action.genDeleteAction(URI(selfHref)))
                .action(Action.genEditAction(URI(selfHref)))
                .toSiren()
    }

    @GetMapping("/v0/courses/{acr}/classes")
    fun getCollection(@PathVariable acr: String): Siren {
        // TODO: for now page and size don't do anything
        val ci = repo.getPage(acr, 0, 3)

        // transforms all CourseInstances into Siren embeds
        val embeds = ci.map {
            SirenBuilder(it)
                    .klass(*CLASS)
                    .rel("item")
                    .link("self", URI(buildHref(it.course, it.calendarTerm)))
                    .toEmbed()
        }

        return SirenBuilder()
                .klass(*CLASS, "collection")
                .entities(embeds)
                .link("self", URI(buildHref(acr)))
                .link("about", URI("/v0/courses/${acr}"))
                .action(Action.genAddItemAction(URI(buildHref(acr))))
                .action(Action.genSearchAction(URI(buildHref(acr))))
                .action(Action(
                        name = "batch-delete",
                        title = "Delete multiple items",
                        method = HttpMethod.GET,
                        href = URI("${buildHref(acr)}{?term,course}"),
                        isTemplated = true,
                        type = "application/vnd.siren+json",
                        fields = listOf(
                                Field(name = "term", type = "text"),
                                Field(name = "page", type = "number")
                        )))
                .toSiren()
    }
}
