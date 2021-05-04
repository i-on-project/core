package org.ionproject.core.classSection

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import java.net.URI

internal class ClassSectionErrorTest : ControllerTester() {

    companion object {
        val notFoundUriByWrongCID = Uri.forClassSectionById(-1, "1920i", "1D")
        val notFoundUriByWrongCalTerm = Uri.forClassSectionById(1, "505050", "1D")
        val notFoundUriByWrongSID = Uri.forClassSectionById(1, "1920i", "505050")
        val validUri = Uri.forClassSectionById(1, "1920i", "1D")
        val badRequestUri = URI("/v0/courses/a/classes/1920i/1D")
    }

    /**
     * Not found course with
     * calterm = 505050
     */
    @Test
    fun getInvalidClassSectionByWrongCID() {
        doGet(notFoundUriByWrongCID) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound }
            }.andReturn()
    }

    /**
     * Not found course with
     * sid = 505050
     */
    @Test
    fun getInvalidClassSectionByWrongCalTerm() {
        doGet(notFoundUriByWrongCalTerm) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound }
            }.andReturn()
    }

    /**
     * Not found course with
     * id = -1
     */
    @Test
    fun getInvalidClassSectionByWrongSID() {
        doGet(notFoundUriByWrongSID) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound }
            }.andReturn()
    }

    /**
     * Requesting course with a content-type "JSON-HOME"
     * that is not supported.
     */
    @Test
    fun getClassSectionWithInvalidContentType() {
        doGet(validUri) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isNotAcceptable } }
            .andReturn()
    }

    /**
     * Bad request with letters instead of a course id
     */
    @Test
    fun getClassSectionWithBadRequest() {
        doGet(badRequestUri) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isBadRequest } }
            .andReturn()
    }
}
