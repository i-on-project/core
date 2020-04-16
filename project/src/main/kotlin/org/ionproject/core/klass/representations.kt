package org.ionproject.core.klass

import org.ionproject.core.common.*
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate
import java.net.URI

val klassClasses = arrayOf("class")

/**
 * Produces the various siren representations of a Class resource.
 */
object KlassToSiren {

    /**
     * Class item representation.
     * Is used as an embedded siren object in a Class Collection.
     */
    fun toSiren(klass: Klass): EmbeddedRepresentation {
        return SirenBuilder()
            .klass(*klassClasses)
            .rel("item")
            .link("self", Uri.forKlassByTerm(klass.courseId, klass.calendarTerm))
            .toEmbed()
    }

    /**
     * Class Collection resource's representation.
     * Supports paging
     */
    fun toSiren(cid: Int, klasses: List<Klass>, page: Int, limit: Int): Siren {
        val selfHref = Uri.forKlasses(cid)

        return SirenBuilder()
            .klass(*klassClasses, "collection")
            .entities(klasses.map { toSiren(it) })
            .link("self", URI("$selfHref?page=$page&limit=$limit"))
            .link("about", Uri.forCourseById(cid))
            .action(Action.genAddItemAction(selfHref.toTemplate()))
            .action(Action.genSearchAction(UriTemplate("$selfHref{?term,course}")))
            .action(Action(
                name = "batch-delete",
                title = "Delete multiple items",
                method = HttpMethod.DELETE,
                href = UriTemplate("$selfHref{?term,course}"),
                isTemplated = true,
                type = Media.SIREN_TYPE,
                fields = listOf(
                    Field(name = "term", type = "text"),
                    Field(name = "page", type = "number")
                )))
            .toSiren()
    }

    /**
     * Fully detailed Class representation.
     */
    fun toSiren(klass: FullKlass): Siren {
        val selfHref = Uri.forKlassByTerm(klass.courseId, klass.calendarTerm)

        // class sections of this class
        val sections = klass.sections.map {
            SirenBuilder(it)
                .klass("class", "section")
                .rel("item")
                .link("self", Uri.forClassSectionById(klass.courseId, klass.calendarTerm, it.id))
                .toEmbed()
        }

        return SirenBuilder(klass)
            .klass(*klassClasses)
            .entities(sections)
            .link("self", selfHref)
            .link("collection", Uri.forKlasses(klass.courseId))
            .action(Action.genDeleteAction(selfHref.toTemplate()))
            .action(Action.genEditAction(selfHref.toTemplate()))
            .toSiren()
    }
}
