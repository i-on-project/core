package org.ionproject.core.klass

import org.ionproject.core.common.Action
import org.ionproject.core.common.EmbeddedRepresentation
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.toTemplate
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.springframework.web.util.UriTemplate

val klassClasses = arrayOf("class")

/**
 * Output models
 */
private data class KlassOutputModel(val courseId: Int, val courseAcr: String?, val calendarTerm: String) {
    companion object {
        fun of(klass: Klass): KlassOutputModel =
            KlassOutputModel(klass.courseId, klass.courseAcr, klass.calendarTerm)
    }
}

/**
 * Class item representation.
 * Is used as an embedded siren object in a Class Collection.
 */
fun Klass.toSiren(): EmbeddedRepresentation {
    return SirenBuilder()
        .klass(*klassClasses)
        .rel("item")
        .link("self", Uri.forKlassByCalTerm(courseId, calendarTerm))
        .toEmbed()
}

/**
 * Class Collection resource's representation.
 * Supports paging
 */
fun List<Klass>.toSiren(cid: Int, page: Int, limit: Int): Siren {
    val selfHref = Uri.forKlasses(cid)

    return SirenBuilder()
        .klass(*klassClasses, "collection")
        .entities(map { klass -> klass.toSiren() })
        .link("self", Uri.forPagingKlass(cid, page, limit))
        .link("about", Uri.forCourseById(cid))
        .action(Action.genAddItemAction(selfHref.toTemplate()))
        .action(Action.genSearchAction(UriTemplate("${selfHref}${Uri.rfcPagingQuery}")))
        .toSiren()
}

/**
 * Fully detailed Class representation.
 */
fun FullKlass.toSiren(): Siren {
    val selfHref = Uri.forKlassByCalTerm(courseId, calendarTerm)

    // class sections of this class
    val sections = sections.map { section ->
        SirenBuilder(section)
            .klass("class", "section")
            .rel("item")
            .link("self", Uri.forClassSectionById(courseId, calendarTerm, section.id))
            .toEmbed()
    }

    return SirenBuilder(KlassOutputModel.of(this))
        .klass(*klassClasses)
        .entities(sections)
        .link("self", selfHref)
        .link("collection", Uri.forKlasses(courseId))
        .action(Action.genDeleteAction(selfHref.toTemplate()))
        .action(Action.genEditAction(selfHref.toTemplate()))
        .toSiren()
}
