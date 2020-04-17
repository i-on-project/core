package org.ionproject.core.calendarTerm.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.CalendarTerm
import org.ionproject.core.common.model.Klass

import org.springframework.http.HttpMethod

fun CalendarTermDetailRepr(ct: CalendarTerm, page: Int, limit: Int) =
    SirenBuilder(CalendarTermPropertiesRepr(ct.calTermId))
        .klass("calendar-term")
        .entities(ct.classes.map { buildSubEntities(it) })
        .action(
            Action(
                name = "Search",
                title = "Search classes in a calendar term",
                method = HttpMethod.GET,
                href = Uri.pagingCalendarTerms,
                isTemplated = true,
                type = Media.SIREN_TYPE,
                fields = listOf(
                    Field(name = "limit", type = "number", klass = "param/limit"),
                    Field(name = "page", type = "number", klass = "param/page")
                )
            )
        )
        .link("self", Uri.forPagingCalTermById(ct.calTermId, page, limit))
        .link("next", Uri.forPagingCalTermById(ct.calTermId, page + 1, limit))
        .link("collection", Uri.forCalTerms())
        .toSiren()

private fun buildSubEntities(cl: Klass) =
    SirenBuilder()
        .klass("class")
        .rel(Uri.relClass)
        .link("self", Uri.forKlassByCalTerm(cl.courseId, cl.calendarTerm))
        .link("collection", Uri.forKlasses(cl.courseId))
        .toEmbed()

data class CalendarTermPropertiesRepr(val name: String)