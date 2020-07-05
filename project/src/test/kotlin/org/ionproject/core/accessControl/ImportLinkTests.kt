package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.representations.ImportLinkRepr
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import java.net.URI

internal class ImportLinkTests: ControllerTester() {

    companion object {
        private val importClassCalendarUrl = URI("/v0/courses/5/classes/1920v/calendar/import?type=T")
        private val importClassSectionCalendarUrl = URI("/v0/courses/5/classes/1920v/LI61D/calendar/import?type=E")
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

        doGet(URI(jsonLink.link)) {
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

        doGet(URI(jsonLink.link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()

    }

    private fun convertToJson(result: String) : ImportLinkRepr {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(result, ImportLinkRepr::class.java)
    }
}
