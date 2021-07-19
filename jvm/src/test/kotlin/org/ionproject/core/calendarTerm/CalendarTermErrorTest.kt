package org.ionproject.core.calendarTerm

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test

internal class CalTermsErrorTest : ControllerTester() {

    companion object {
        val cals = Uri.forCalTerms()
        val notFoundUriByWrongTerm = Uri.forCalTermById("1921231230i")
        val validUri = Uri.forCalTermById("1920i")
    }

    /**
     * Not found calendar term with
     * calterm = 1921231230i
     */
    @Test
    fun getInvalidCalTerm() {
        doGet(notFoundUriByWrongTerm) {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isNotFound() }
            }.andReturn()
    }

    /**
     * Requesting course with a content-type "JSON-HOME"
     * that is not supported.
     */
    @Test
    fun getCalTermWithInvalidContentType() {
        doGet(validUri) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect { status { isNotAcceptable() } }
            .andReturn()
    }

    @Test
    fun getCalTermsInvalidContentType() {
        doGet(cals) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect { status { isNotAcceptable() } }
            .andReturn()
    }
}
