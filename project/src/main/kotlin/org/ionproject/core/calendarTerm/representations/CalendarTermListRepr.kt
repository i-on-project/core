package org.ionproject.core.calendarTerm.representations

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.*
import org.springframework.http.HttpMethod

/**
 * Siren representation generators
 */
fun List<CalendarTerm>.toCalendarTermListRepr(page: Int, limit: Int) =
    SirenBuilder()
        .klass("calendar-term", "collection")
        .entities(this.map { calendarTerm -> calendarTerm.toEmbed() })
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

private fun CalendarTerm.toEmbed() =
    SirenBuilder(CalendarTermOutputModel(calTermId))
        .klass("term")
        .rel("item")
        .link("self", Uri.forCalTermById(calTermId))
        .link("collection", Uri.forCalTerms())
        .toEmbed()