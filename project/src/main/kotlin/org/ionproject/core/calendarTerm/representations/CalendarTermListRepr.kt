package org.ionproject.core.calendarTerm.representations

import org.ionproject.core.common.*
import org.ionproject.core.common.model.CalendarTerm
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate

fun CalendarTermListRepr(cts: List<CalendarTerm>, page: Int, limit: Int) =
    SirenBuilder()
        .klass("calendar-term", "collection")
        .entities(cts.map { buildSubEntities(it) })
        .action(
            Action(
                name = "Search",
                title = "Search Items",
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
        .link("self", Uri.forPagingCalTerms(page, limit))
        .link("next", Uri.forPagingCalTerms(page + 1, limit))
        .let {
            {
                if (page > 0)
                    it.link("previous", Uri.forPagingCalTerms(page - 1, limit))
                it
            }()
        }.toSiren()

private fun buildSubEntities(ct: CalendarTerm) =
    SirenBuilder(CalendarTermPropertiesRepr(ct.calTermId))
        .klass("term")
        .rel("item")
        .link("self", Uri.forCalTermById(ct.calTermId))
        .link("collection", Uri.forCalTerms())
        .toEmbed()