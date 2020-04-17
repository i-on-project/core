package org.ionproject.core.klass

import org.ionproject.core.common.*
import org.springframework.web.util.UriTemplate

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
            .link("self", Uri.forKlassByCalTerm(klass.courseId, klass.calendarTerm))
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
            .link("self", Uri.forPagingKlass(cid, page, limit))
            .link("about", Uri.forCourseById(cid))
            .action(Action.genAddItemAction(selfHref.toTemplate()))
            .action(Action.genSearchAction(UriTemplate("${selfHref}${Uri.rfcPagingQuery}")))
            .toSiren()
    }

    /**
     * Fully detailed Class representation.
     */
    fun toSiren(klass: FullKlass): Siren {
        val selfHref = Uri.forKlassByCalTerm(klass.courseId, klass.calendarTerm)

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
