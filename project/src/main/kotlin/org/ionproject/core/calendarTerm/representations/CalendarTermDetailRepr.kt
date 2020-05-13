package org.ionproject.core.calendarTerm.representations

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.*
import org.ionproject.core.klass.model.Klass
import org.springframework.http.HttpMethod

/**
 * Output models
 */
data class CalendarTermOutputModel(val name: String)

/**
 * Siren representation generators
 */
fun CalendarTerm.toCalendarTermDetailRepr(page: Int, limit: Int) =
  SirenBuilder(CalendarTermOutputModel(calTermId))
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
    .link("self", href = Uri.forPagingCalTermById(calTermId, page, limit))
    .link("next", href = Uri.forPagingCalTermById(calTermId, page + 1, limit))
    .link("collection", href = Uri.forCalTerms())
    .toSiren()

private fun Klass.toEmbed() =
  SirenBuilder()
    .klass("class")
    .rel(Uri.relClass)
    .link("self", href = Uri.forKlassByCalTerm(courseId, calendarTerm))
    .link("collection", href = Uri.forKlasses(courseId))
    .toEmbed()
