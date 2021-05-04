package org.ionproject.core.classSection

import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri

val classSectionClasses = arrayOf("class", "section")

fun ClassSection.toSiren(): Siren {
    val selfHref = Uri.forClassSectionById(courseId, calendarTerm, id)
    return SirenBuilder(this)
        .klass(*classSectionClasses)
        .entities(buildSubEntities(courseId, calendarTerm, id))
        .link("self", href = selfHref)
        .link("collection", href = Uri.forKlassByCalTerm(courseId, calendarTerm))
        .toSiren()
}

fun buildSubEntities(courseId: Int, calendarTerm: String, id: String) =
    SirenBuilder()
        .klass("calendar")
        .rel(Uri.relCalendar)
        .link("self", href = Uri.forCalendarByClassSection(courseId, calendarTerm, id))
        .toEmbed()
