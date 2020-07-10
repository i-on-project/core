package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.representations.ImportLinkRepr
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URI

internal class ImportLinkTests: ControllerTester() {

    companion object {
        private val importClassCalendarUrl = Uri.forImportClassCalendar(5, "1920v")
        private val importClassSectionCalendarUrl = Uri.forImportClassSectionCalendar(5, "1920v", "LI61D")

        private val importClassCalendarNotFoundUrl = Uri.forImportClassCalendar(1238949324, "1920v")

        private val importClassSectionCalendarDuplicate = Uri.forImportClassSectionCalendar(4, "1920v", "LI61D")
    }

    /**
     * Tries to generate an import link for a class calendar
     */
    @Test
    fun generateClassCalendarImportUrl() {
        val linkResult = doGet(importClassCalendarUrl) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
            .response
            .contentAsString

        val jsonLink = convertToJson(linkResult)

        // Spring boot delivers the requests directly to a Servlet
        // Host:Port will not be considered
        val link = jsonLink.url.dropWhile { c -> c != '/' }
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()

    }

    /**
     * Tries to generate an import link for a class section calendar
     */
    @Test
    fun generateClassSectionCalendarImportUrl() {
        val linkResult = doGet(importClassSectionCalendarUrl) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
            .response
            .contentAsString

        val jsonLink = convertToJson(linkResult)

        // Spring boot delivers the requests directly to a Servlet
        // Host:Port will not be considered
        val link = jsonLink.url.dropWhile { c -> c != '/' }
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()

    }

    /**
     * Tries to issue an import URL for an invalid/non existent resource
     * it should respond Not Found to avoid Denial of Service
     */
    @Test
    fun generateInexistentResourceImportUrl() {
         doGet(importClassCalendarNotFoundUrl) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isNotFound } }
            .andReturn()
    }


    /**
     * Tries to issue an import URL two times and check if its shared,
     * if its two different access tokens DOS is possible and should be corrected.
     */
    @Test
    fun generateImportUrlDuplicate() {
        val result1 = doGet(importClassSectionCalendarDuplicate) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andReturn()
            .response
            .contentAsString

        val link1 = convertToJson(result1)

        val result2 = doGet(importClassSectionCalendarDuplicate) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andReturn()
            .response
            .contentAsString

        val link2 = convertToJson(result2)

        Assertions.assertEquals(link1.url, link2.url)
    }


    private fun convertToJson(result: String) : ImportLinkRepr {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(result, ImportLinkRepr::class.java)
    }
}
