package org.ionproject.core.readApi.classSection

import org.ionproject.core.readApi.common.*
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class ClassSectionControllerTest : ControllerTester() {

    companion object {
        fun getClassSection(): ClassSection =
            ClassSection(1, "SL", "1718v", "1D")
    }

    /**
     * 200 OK
     */
    @Test
    fun getClassSection_shouldRespondWithSirenType() {
        isValidSiren(Uri.forClassSectionById(1, "2021v", "1D")).andReturn()
    }

    @Test
    fun getClass_shouldRespondWithTheExactSirenRepresentationOfClass() {
        val cs = getClassSection()
        val selfHref = Uri.forClassSectionById(cs.courseId, cs.calendarTerm, cs.id)

        val expected = SirenBuilder(this)
            .klass(*classSectionClasses)
            .entities(
                SirenBuilder()
                    .klass("calendar")
                    .rel(Uri.relCalendar)
                    .link("self", href = Uri.forCalendarByClassSection(cs.courseId, cs.calendarTerm, cs.id))
                    .toEmbed()
            )
            .link("self", href = selfHref)
            .link("collection", href = Uri.forKlassByCalTerm(cs.courseId, cs.calendarTerm))
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

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
