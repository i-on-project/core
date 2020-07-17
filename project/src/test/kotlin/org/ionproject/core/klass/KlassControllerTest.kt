package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.common.*
import org.ionproject.core.fluentAdd
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.matchMvc
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.get
import org.springframework.web.util.UriTemplate

internal class KlassControllerTest : ControllerTester() {

    companion object {
        fun getFullKlass(): FullKlass {
            val cid = 1
            val cacr = "SL"
            val calTerm = "1718v"
            return FullKlass(
                cid, cacr, calTerm, sections = listOf(
              ClassSection(cid, cacr, calTerm, "1D"),
              ClassSection(cid, cacr, calTerm, "1N"),
              ClassSection(cid, cacr, calTerm, "2D")
            )
            )
        }

        fun getClassCollection(): List<Klass> {
            val cid = 1
            val cacr = "SL"
            val klass1 = Klass(cid, cacr, "1718i")
            val klass2 = Klass(cid, cacr, "1718v")
            return listOf(klass1, klass2)
        }
    }

    /**
     * 200 OK tests
     */
    @Test
    fun getClass_shouldRespondWithSirenType() {
        isValidSiren(Uri.forKlassByCalTerm(2, "1920v")).andReturn()
    }

    @Test
    fun getClassCollection_shouldRespondWithSirenType() {
        isValidSiren(Uri.forKlasses(1)).andReturn()
    }

    @Test
    fun getClass_shouldRespondWithTheExactSirenRepresentationOfClass() {
        val klass = getFullKlass()
        val selfHref = Uri.forKlassByCalTerm(klass.courseId, klass.calendarTerm)

        val entities = klass.sections.map { section ->
            SirenBuilder(section)
                .klass("class", "section")
                .rel("item")
                .link("self", href = Uri.forClassSectionById(klass.courseId, klass.calendarTerm, section.id))
                .toEmbed()
        }.toMutableList().fluentAdd(
            SirenBuilder()
                .klass("calendar")
                .rel(Uri.relCalendar)
                .link("self", href = Uri.forCalendarByClass(klass.courseId, klass.calendarTerm))
                .toEmbed()
        )

        val expected = SirenBuilder(klass)
            .klass(*klassClasses)
            .entities(entities)
            .link("self", href = selfHref)
            .link("collection", href = Uri.forKlasses(klass.courseId))
            .toSiren()

        isValidSiren(selfHref)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getClasses_shouldRespondWithTheExactSirenRepresentationOfClassCollection() {
        val list = getClassCollection()
        val page = 0
        val limit = 2
        val cid = list[0].courseId
        val selfHrefPage = Uri.forPagingKlass(cid, page, limit)
        val selfHref = Uri.forKlasses(cid)

        data class OutputModel(val cid: Int)

        val expected = SirenBuilder(OutputModel(cid))
            .klass(*klassClasses, "collection")
            .entities(list.map { klass ->
                SirenBuilder()
                    .klass(*klassClasses)
                    .rel("item")
                    .link("self", href = Uri.forKlassByCalTerm(klass.courseId, klass.calendarTerm))
                    .toEmbed()
            })
            .action(
                Action(
                    name = "search",
                    title = "Search items",
                    method = HttpMethod.GET,
                    href = UriTemplate("${selfHref}${Uri.rfcPagingQuery}"),
                    isTemplated = true,
                    type = Media.SIREN_TYPE,
                    fields = listOf(
                        Field(name = "limit", type = "number", klass = "param/limit"),
                        Field(name = "page", type = "number", klass = "param/page")
                    )
                )
            )
            .link("self", href = selfHrefPage)
            .link("about", href = Uri.forCourseById(cid))
            .toSiren()

        isValidSiren(selfHrefPage)
            .andDo { print() }
            .andExpect { expected.matchMvc(this) }
            .andReturn()
    }

    @Test
    fun getPage_shouldRespondWithTheCorrectNumberOfItems() {
        var limit = 1
        mocker.get("${Uri.forKlasses(1)}?limit=$limit") {
            accept = Media.MEDIA_SIREN
            header("Authorization", readTokenTest)
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
            jsonPath("$.entities.length()") { value(limit) }
        }

        limit = 2
        mocker.get("${Uri.forKlasses(1)}?limit=$limit") {
            accept = Media.MEDIA_SIREN
            header("Authorization", readTokenTest)
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
            jsonPath("$.entities.length()") { value(limit) }
        }
    }

}