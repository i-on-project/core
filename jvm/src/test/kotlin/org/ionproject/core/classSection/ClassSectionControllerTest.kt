package org.ionproject.core.classSection

import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test

internal class ClassSectionControllerTest : ControllerTester() {

    companion object {
        fun getClassSection(): ClassSection =
            ClassSection(1, "SL", "1718v", "1D", 1)
    }

    /**
     * 200 OK
     */
    @Test
    fun getClassSection_shouldRespondWithSirenType() {
        isValidSiren(Uri.forClassSectionById(2, "1920v", "LI61D")).andReturn()
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
            .toSiren()

        isValidSiren(selfHref)
            .andDo { println() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }
}
