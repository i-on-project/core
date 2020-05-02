package org.ionproject.core.klass

import org.ionproject.core.common.*
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.springframework.http.HttpMethod
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
        .action(
            Action(
                name = "add-item",
                title = "Add Item",
                method = HttpMethod.POST,
                href = selfHref.toTemplate(),
                isTemplated = false,
                type = Media.APPLICATION_JSON
            )
        )
        .action(
            Action(
                name = "search",
                title = "Search items",
                method = HttpMethod.GET,
                href = UriTemplate("${selfHref}${Uri.rfcPagingQuery}"),
                isTemplated = true,
                type = Media.APPLICATION_JSON,
                fields = listOf(
                    Field(name = "limit", type = "number", klass = "param/limit"),
                    Field(name = "page", type = "number", klass = "param/page")
                )
            )
        )
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
        .action(
            Action(
                name = "delete",
                href = selfHref.toTemplate(),
                method = HttpMethod.DELETE,
                type = Media.ALL,
                isTemplated = false
            )
        )
        .action(
            Action(
                name = "edit",
                href = selfHref.toTemplate(),
                method = HttpMethod.PATCH,
                type = Media.APPLICATION_JSON,
                isTemplated = false
            )
        )
        .toSiren()
}