package org.ionproject.core.klass

import org.ionproject.core.common.*
import org.springframework.http.HttpMethod

val klassClasses = arrayOf("class")

open class KlassItemOutputModel(
    val klass: Klass) {

    fun toSiren(): EmbeddedRepresentation {
        return SirenBuilder(klass)
            .klass(*klassClasses)
            .rel("item")
            .link("self", Uri.forKlassByTerm(klass.course, klass.calendarTerm))
            .toEmbed()
    }
}

class KlassCollectionOutputModel(
    val acr: String,
    val klasses: List<Klass>) {

    fun toSiren(): Siren {
        val selfHref = Uri.forKlasses(acr)
        return SirenBuilder()
            .klass(*klassClasses, "collection")
            .entities(klasses.map { KlassItemOutputModel(it).toSiren() })
            .link("self", selfHref)
            .link("about", Uri.forCourseByAcr(acr))
            .action(Action.genAddItemAction(selfHref))
            .action(Action.genSearchAction(selfHref))
            .action(Action(
                name = "batch-delete",
                title = "Delete multiple items",
                method = HttpMethod.DELETE,
                href = selfHref,//"{?term,course}",
                isTemplated = true,
                type = "application/vnd.siren+json",
                fields = listOf(
                    Field(name = "term", type = "text"),
                    Field(name = "page", type = "number")
                )))
            .toSiren()
    }

}

class FullKlassOutputModel(
    val klass: FullKlass) {

    fun toSiren(): Siren {
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
