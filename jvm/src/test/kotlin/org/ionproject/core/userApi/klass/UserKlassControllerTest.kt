package org.ionproject.core.userApi.klass

import org.ionproject.core.common.Action
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.DUMMY_USER_ACCESS_TOKEN
import org.ionproject.core.utils.matchMvc
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.util.UriTemplate

internal class UserKlassControllerTest : ControllerTester() {

    @Test
    fun `Get subscribed classes without Authorization header`() {
        val uri = Uri.forUserClasses()
        doGet(uri)
            .andExpect {
                status { `is`(401) }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                }
            }
            .andReturn()
    }

    @Test
    fun `Get subscribed sections without Authorization header`() {
        val uri = Uri.forUserSections()
        doGet(uri)
            .andExpect {
                status { `is`(401) }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                }
            }
            .andReturn()
    }

    @Test
    fun `Subscribe to class sections, check class and check sections`() {
        val classId = 18
        val sectionId = "1D"
        val classSectionUri = Uri.forUserClassSection(classId, sectionId)
        doPut(classSectionUri) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        val classUri = Uri.forUserClass(classId)
        isValidSiren(classUri, DUMMY_USER_ACCESS_TOKEN)
            .andReturn()

        val userSections = Uri.forUserSections()
        isValidSiren(userSections, DUMMY_USER_ACCESS_TOKEN).andExpect {
            jsonPath("$.entities[0]") { exists() }
        }.andReturn()

        doDelete(classUri) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()
    }

    @Test
    fun `Subscribe to class and check class`() {
        val classId = 11
        val userClass = Uri.forUserClass(classId)
        doPut(userClass) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        isValidSiren(userClass, DUMMY_USER_ACCESS_TOKEN)
            .andReturn()
    }

    @Test
    fun `Unsubscribe class and check class section`() {
        val classId = 16
        val sectionId = "1D"
        val userClassSection = Uri.forUserClassSection(classId, sectionId)
        doPut(userClassSection) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        val userClass = Uri.forUserClass(classId)
        doDelete(userClass) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        doGet(userClassSection) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { `is`(404) }
        }.andReturn()
    }

    @Test
    fun `Unsubscribe class section, check class section and check class`() {
        val classId = 16
        val sectionId = "1D"
        val userClassSection = Uri.forUserClassSection(classId, sectionId)
        doPut(userClassSection) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        doDelete(userClassSection) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        val userClass = Uri.forUserClass(classId)
        isValidSiren(userClass, DUMMY_USER_ACCESS_TOKEN)
            .andReturn()

        doGet(userClassSection) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { `is`(404) }
        }.andReturn()
    }

    @Test
    fun `Class actions with subscribe action`() {
        val classId = 13
        val classActions = Uri.forUserClassActions(classId)
        val userClass = Uri.forUserClass(classId)

        val entity = SirenBuilder()
            .klass("user", "class", "action")
            .action(
                Action(
                    name = "subscribe",
                    href = UriTemplate(userClass.toString()),
                    title = "Subscribe to Class",
                    method = HttpMethod.PUT
                )
            )
            .link("self", href = classActions)
            .toSiren()

        isValidSiren(classActions, DUMMY_USER_ACCESS_TOKEN).andExpect {
            entity.matchMvc(this)
        }.andReturn()
    }

    @Test
    fun `Class actions with unsubscribe action`() {
        val classId = 14
        val classActions = Uri.forUserClassActions(classId)
        val userClass = Uri.forUserClass(classId)

        doPut(userClass) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        val entity = SirenBuilder()
            .klass("user", "class", "action")
            .action(
                Action(
                    name = "unsubscribe",
                    href = UriTemplate(userClass.toString()),
                    title = "Unsubscribe From Class",
                    method = HttpMethod.DELETE
                )
            )
            .link("self", href = classActions)
            .toSiren()

        isValidSiren(classActions, DUMMY_USER_ACCESS_TOKEN).andExpect {
            entity.matchMvc(this)
        }.andReturn()
    }

    @Test
    fun `Class section actions with subscribe action`() {
        val classId = 7
        val sectionId = "1D"
        val classSectionActions = Uri.forUserClassSectionActions(classId, sectionId)
        val userClassSection = Uri.forUserClassSection(classId, sectionId)

        val entity = SirenBuilder()
            .klass("user", "class", "section", "action")
            .action(
                Action(
                    name = "subscribe",
                    href = UriTemplate(userClassSection.toString()),
                    title = "Subscribe to Class Section",
                    method = HttpMethod.PUT
                )
            )
            .link("self", href = classSectionActions)
            .toSiren()

        isValidSiren(classSectionActions, DUMMY_USER_ACCESS_TOKEN).andExpect {
            entity.matchMvc(this)
        }.andReturn()
    }

    @Test
    fun `Class section actions with unsubscribe action`() {
        val classId = 7
        val sectionId = "2D"
        val classSectionActions = Uri.forUserClassSectionActions(classId, sectionId)
        val userClassSection = Uri.forUserClassSection(classId, sectionId)

        doPut(userClassSection) {
            header(HttpHeaders.AUTHORIZATION, DUMMY_USER_ACCESS_TOKEN)
        }.andExpect {
            status { is2xxSuccessful }
        }.andReturn()

        val entity = SirenBuilder()
            .klass("user", "class", "section", "action")
            .action(
                Action(
                    name = "unsubscribe",
                    href = UriTemplate(userClassSection.toString()),
                    title = "Unsubscribe From Class Section",
                    method = HttpMethod.DELETE
                )
            )
            .link("self", href = classSectionActions)
            .toSiren()

        isValidSiren(classSectionActions, DUMMY_USER_ACCESS_TOKEN).andExpect {
            entity.matchMvc(this)
        }.andReturn()
    }
}
