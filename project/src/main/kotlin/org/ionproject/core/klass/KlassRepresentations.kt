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

private data class KlassCollectionOutputModel(val cid: Int)

private data class KlassItemOutputModel(val calendarTerm: String)

/**
 * Class item representation.
 * Is used as an embedded siren object in a Class Collection.
 */
fun Klass.toSiren(): EmbeddedRepresentation {
    return SirenBuilder(KlassItemOutputModel(this.calendarTerm))
        .klass(*klassClasses)
        .rel("item")
        .link("self", href = Uri.forKlassByCalTerm(courseId, calendarTerm))
        .toEmbed()
}

/**
 * Class Collection resource's representation.
 * Supports paging
 */
fun List<Klass>.toSiren(cid: Int, page: Int, limit: Int): Siren {
    val selfHref = Uri.forKlasses(cid)

    return SirenBuilder(KlassCollectionOutputModel(cid))
        .klass(*klassClasses, "collection")
        .entities(map { klass -> klass.toSiren() })
        .link("self", href = Uri.forPagingKlass(cid, page, limit))
        .link("about", href = Uri.forCourseById(cid))
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
            .link("self", href = Uri.forClassSectionById(courseId, calendarTerm, section.id))
            .toEmbed()
    }

    fun calTermEntity(courseId: Int, calTerm: String): EmbeddedRepresentation =
        SirenBuilder()
            .klass("calendar")
            .rel(Uri.relCalendar)
            .link("self", href = Uri.forCalendarByClass(courseId, calTerm))
            .toEmbed()

    fun buildSubEntities(sections: List<EmbeddedRepresentation>): MutableList<EmbeddedRepresentation> {
        val listSubEntities = sections.toMutableList()
        listSubEntities.add(calTermEntity(this.courseId, this.calendarTerm))
        return listSubEntities
    }

    return SirenBuilder(KlassOutputModel.of(this))
        .klass(*klassClasses)
        .entities(buildSubEntities(sections))
        .link("self", href = selfHref)
        .link("collection", href = Uri.forKlasses(courseId))
        .toSiren()
}
