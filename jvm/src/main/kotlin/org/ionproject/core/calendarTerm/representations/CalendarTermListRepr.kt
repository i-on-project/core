package org.ionproject.core.calendarTerm.representations

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.Media
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.springframework.http.HttpMethod
import java.time.LocalDateTime

private data class CalendarTermShortProps(val name: String, val startDate: LocalDateTime, val endDate: LocalDateTime)

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
        .link("self", href = Uri.forPagingCalTerms(page, limit))
        .link("next", href = Uri.forPagingCalTerms(page + 1, limit))
        .let {
            if (page > 0)
                it.link("previous", href = Uri.forPagingCalTerms(page - 1, limit))

            it
        }.toSiren()

private fun CalendarTerm.toEmbed() =
    SirenBuilder(CalendarTermShortProps(calTermId, startDate, endDate))
        .klass("term")
        .rel("item")
        .link("self", href = Uri.forCalTermById(calTermId))
        .link("collection", href = Uri.forCalTerms())
        .toEmbed()
