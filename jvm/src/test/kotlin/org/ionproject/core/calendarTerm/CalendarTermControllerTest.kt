package org.ionproject.core.calendarTerm

import org.ionproject.core.calendarTerm.model.CalendarTerm
import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.Media
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import java.time.OffsetDateTime.now

internal class CalendarTermControllerTest : ControllerTester() {
    companion object {
        fun getCalendarTerm_Mock(): CalendarTerm {
            val dateTime = now().toLocalDateTime()
            return CalendarTerm("1920i", dateTime, dateTime)
        }
    }

    @Test
    fun getCalendarTerm() {
        isValidSiren(Uri.forCalTerms()).andReturn()
    }

    @Test
    fun getCalendarTermById() {
        isValidSiren(Uri.forCalTermById("1920i")).andReturn()
    }

    data class CalendarTermOutputModel(val name: String)

    @Test
    fun getCalendarTerm_ResponseCompare() {
        val ct = getCalendarTerm_Mock()
        val selfHref = Uri.forCalTermById(ct.calTermId)

        val subentities = ct.classes.map {
            SirenBuilder()
                .klass("class")
                .rel(Uri.relClass)
                .link("self", href = Uri.forKlassByCalTerm(it.courseId, ct.calTermId))
                .link("collection", href = Uri.forKlasses(it.courseId))
                .toEmbed()
        }

        val expected =
            SirenBuilder(CalendarTermOutputModel(ct.calTermId))
                .klass("calendar-term")
                .entities(subentities)
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
                .link("self", href = Uri.forPagingCalTermById(ct.calTermId, 0, 10))
                .link("next", href = Uri.forPagingCalTermById(ct.calTermId, 0 + 1, 10))
                .link("collection", href = Uri.forCalTerms())
                .toSiren()

        isValidSiren(selfHref, true)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
