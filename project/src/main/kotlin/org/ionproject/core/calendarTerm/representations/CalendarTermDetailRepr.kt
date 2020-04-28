package org.ionproject.core.calendarTerm.representations

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.Media
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.klass.model.Klass
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class CalendarTermPropertiesRepr(val name: String)

/**
 * Siren representation generators
 */
fun CalendarTerm.toCalendarTermDetailRepr(page: Int, limit: Int) =
    SirenBuilder(CalendarTermPropertiesRepr(calTermId))
        .klass("calendar-term")
        .entities(classes.map { klass -> klass.toEmbed() })
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
        .link("self", Uri.forPagingCalTermById(calTermId, page, limit))
        .link("next", Uri.forPagingCalTermById(calTermId, page + 1, limit))
        .link("collection", Uri.forCalTerms())
        .toSiren()

private fun Klass.toEmbed() =
    SirenBuilder()
        .klass("class")
        .rel(Uri.relClass)
        .link("self", Uri.forKlassByCalTerm(courseId, calendarTerm))
        .link("collection", Uri.forKlasses(courseId))
        .toEmbed()
