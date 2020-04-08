package org.ionproject.core.klass

import org.ionproject.core.common.*
import org.springframework.http.HttpMethod
import java.net.URI

val klassClasses = arrayOf("class")

object KlassToSiren {
    fun toSiren(klass: Klass): EmbeddedRepresentation {
        return SirenBuilder(klass)
            .klass(*klassClasses)
            .rel("item")
            .link("self", Uri.forKlassByTerm(klass.course, klass.calendarTerm))
            .toEmbed()
    }

    fun toSiren(acr: String, klasses: List<Klass>, page: Int, size: Int): Siren {
        val selfHref = Uri.forKlasses(acr)
        return SirenBuilder()
            .klass(*klassClasses, "collection")
            .entities(klasses.map { toSiren(it) })
            .link("self", URI("$selfHref?page=$page&size=$size"))
            .link("about", Uri.forCourseByAcr(acr))
            .action(Action.genAddItemAction(selfHref))
            .action(Action.genSearchAction(URI("$selfHref?term,course")))
            .action(Action(
                name = "batch-delete",
                title = "Delete multiple items",
                method = HttpMethod.DELETE,
                href = URI("$selfHref?term,course"),
                isTemplated = true,
                type = "application/vnd.siren+json",
                fields = listOf(
                    Field(name = "term", type = "text"),
                    Field(name = "page", type = "number")
                )))
            .toSiren()
    }

    fun toSiren(klass: FullKlass): Siren {
        val selfHref = Uri.forKlassByTerm(klass.course, klass.calendarTerm)

        val sections = klass.sections.map {
            SirenBuilder(it)
                .klass("class", "section")
                .rel("item")
                .link("self", Uri.forClassSectionById(klass.course, klass.calendarTerm, it.id))
                .toEmbed()
        }

        return SirenBuilder(klass)
            .klass(*klassClasses)
            .entities(sections)
            .link("self", selfHref)
            .link("collection", Uri.forKlasses(klass.course))
            .action(Action.genDeleteAction(selfHref))
            .action(Action.genEditAction(selfHref))
            .toSiren()
    }
}
