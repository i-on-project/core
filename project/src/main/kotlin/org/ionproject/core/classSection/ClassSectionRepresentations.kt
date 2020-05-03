package org.ionproject.core.classSection

import org.ionproject.core.common.Action
import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.toTemplate
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

val classSectionClasses = arrayOf("class", "section")

fun ClassSection.toSiren(): Siren {
    val selfHref = Uri.forClassSectionById(courseId, calendarTerm, id)
    return SirenBuilder(this)
            .klass(*classSectionClasses)
            .entities(listOf(buildSubEntities(courseId,calendarTerm,id)))
            .link("self", href = selfHref)
            .link("collection", href = Uri.forKlassByCalTerm(courseId, calendarTerm))
            .action(
                Action(
                        name = "delete",
                        href = selfHref.toTemplate(),
                        method = HttpMethod.DELETE,
                        type = Media.ALL,
                        isTemplated = false,
                        fields = listOf()
                )
            )
            .toSiren()
}

fun buildSubEntities(courseId: Int, calendarTerm: String, id: String) =
        SirenBuilder()
                .klass("calendar")
                .rel(Uri.relCalendar)
                .link("self", href = Uri.forCalendarByClassSection(courseId, calendarTerm, id))
                .toEmbed()